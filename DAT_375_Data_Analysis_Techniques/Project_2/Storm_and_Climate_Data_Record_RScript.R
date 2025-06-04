install.packages("tidyverse");
install.packages("lubridate");
install.packages("tfplot");
install.packages("tfplot");
# Load required libraries
library(tidyverse)
library(lubridate)
library("tframe");
library("tfplot");

# Set work directory
setwd("C:/Users/Goral/OneDrive/Desktop/SNHU/Term 12 - 25C3/DAT-375/Project_2") 

# Read the CSV
crimestormdataQ <- read.csv("crimeStormQ.csv")
str(crimestormdataQ)      # Show field types
head(crimestormdataQ)     # Show some results
summary(crimestormdataQ)  # Explain data set

crimenostormdataQ <- read.csv("crimenostormQ.csv")
str(crimenostormdataQ)
head(crimestormdataQ)
summary(crimenostormdataQ)

# Convert Date column to proper date format
crimestormdataQ$Date <- mdy(crimestormdataQ$Date)
crimenostormdataQ$Date <- mdy(crimenostormdataQ$Date)

# Check for empty values
sum(is.na(crimestormdataQ$Loss))
sum(is.na(crimenostormdataQ$Loss))

# Prepare data for time series plotting. Sum the monthly victim loss.
z<-ts(cumsum(crimestormdataQ$Loss)/1000,start=c(2017,1), frequency=12)
x<-ts(cumsum(crimenostormdataQ$Loss)/1000,start=c(2017,1), frequency=12)

# Plot and visualize the data sets.
tfplot(z,x,
       ylab="Victim Loss in K$",
       xlab="By Month by Year",
       title="Victim Loss From Crimes for Jan 2017 - Dec 2019",
       subtitle = "Cumulative Loss in Thousands of Dollars",
       legend=c("Crimes During Storms (black)", "Crimes When No Storms (red)"),
       source="Source: DAT Data")


# ==========================================================
# Run t-test to compare if loss is higher during storms.
# ==========================================================
t.test(crimestormdataQ$Loss, crimenostormdataQ$Loss,
       alternative = "greater")


# ==========================================================
# Aggregate and plot the cumulative yearly loss
# ==========================================================
# Extract year from converted crimestormdataQ$Date.
crimestormdataQ$Year <- format(crimestormdataQ$Date, "%Y")

# Aggregate loss by year and output cumulative yearly loss.
yearly_loss <- aggregate(Loss ~ Year, data = crimestormdataQ, sum)
print(yearly_loss)

barplot(yearly_loss$Loss,
        names.arg = yearly_loss$Year,
        main = "Total Crime Loss During Storms by Year",
        ylab = "Total Loss ($)",
        col = "skyblue")


# ==========================================================
# Plot all crime/loss events and the potential trend.
# ==========================================================
# Plot all daily losses during storms by year with a smooth trend over the years.
ggplot(crimestormdataQ, aes(x = Date, y = Loss)) +
  geom_point(alpha = 0.3, color = "blue") +
  geom_smooth(method = "loess", color = "red") +
  labs(title = "Monthly Loss Over Time", x = "Date", y = "Loss (USD)") +
  theme_minimal()


# ==========================================================
# Plot all crime/loss events in a more detailed monthly view
# ==========================================================
# Extract Year and Month from crimestormdataQ
crimestormdataQ <- crimestormdataQ %>%
  mutate(
    Year = year(Date),
    Month = month(Date),
  )

# Aggregate total loss per month by Date
monthly_loss <- crimestormdataQ %>%
  group_by(Date) %>%
  summarise(
    Total_Loss = sum(Loss), .groups = "drop"
  )

# Plot total monthly loss over time
ggplot(monthly_loss, aes(x = Date, y = Total_Loss)) +
  geom_col(fill = "steelblue") +
  labs(
    title = "Total Monthly Crime Loss Over Time",
    x = "Month/Year",
    y = "Total Loss (USD)"
  ) +
  scale_x_date(date_labels = "%b %Y", date_breaks = "1 months") +
  theme_minimal() +
  theme(axis.text.x = element_text(angle = 75, hjust = 1))


# ==========================================================
# Aggregate and accumulate monthly loss across all years and plot
# ==========================================================
# Convert months from num type to calendar month and store in Month_STR.
crimestormdataQ <- crimestormdataQ %>%
  mutate(Month_STR = month(Date, label = TRUE, abbr = TRUE))

# Aggregate sum of loss per calendar month across all years
monthly_total_loss <- crimestormdataQ %>%
  group_by(Month_STR) %>%
  summarise(Total_Loss = sum(Loss, na.rm = TRUE), .groups = "drop")

# Plot aggregated results as a line chart
ggplot(monthly_total_loss, aes(x = Month_STR, y = Total_Loss, group = 1)) +
  geom_line(color = "steelblue", linewidth = 1) +
  geom_point(color = "darkred", size = 2) +
  labs(
    title = "Total Crime Loss by Month (Sum of All Years)",
    x = "Month",
    y = "Total Loss (USD)"
  ) +
  theme_minimal()