housing <- read.csv(file="housing_v2.csv", header=TRUE, sep=",")

# converting appropriate variables to factors  
housing_fctrd <- within(housing, {
   view <- factor(view)
   backyard <- factor(backyard)
   renovated <- factor(renovated)
})

# number of columns
ncol(housing_fctrd)

# number of rows
nrow(housing_fctrd)


# Print the first five rows
print("Dataset Preview:")
head(housing_fctrd, 5)

# Subset the original data set to select some variables and create a new data set
housing_vars <- c("price","sqft_living","age")
housing_subset <- housing_fctrd[housing_vars]

# Privew the housing_subset
print("housing_subset")
head(housing_subset, 5)

# Display the correlation matrix
cat("\nCorrelation Analysis:")
corr_matrix <- cor(housing_subset, method = "pearson")
round(corr_matrix, 4)
cat("\n")

cor_price_sqft <- cor(housing_subset$price, housing_subset$sqft_living, method = "pearson")
print(paste("Correlation between Price and Living Area: r =", round(cor_price_sqft, 3), "suggests a moderate to strong positive relationship."))
cat("This means that as living area increases, price tends to increase as well.\n\n")

# Fit a linear model for price VS sqft_living
lm1 <- lm(price ~ sqft_living, data = housing_subset)

# Scatterplot: Price vs. Living Area
plot(housing_subset$sqft_living, housing_subset$price, 
     main = "Price vs. Living Area", 
     xlab = "Living Area (sqft)", 
     ylab = "Price ($)", 
     col = "blue", pch = 16)

# Add trendline to the plot
abline(lm1, col = "black", lwd = 2)
legend("topleft", legend = paste("Correlation:", round(cor_price_sqft, 3)), bty = "n")


cor_price_age <- cor(housing_subset$price, housing_subset$age, method = "pearson")
print(paste("Correlation between Price and Age: r =", round(cor_price_age, 3), "suggests a very weak negative relationship."))
cat("This means that as the age of the home increases, the price tends to decrease slightly.\n\n")

# Fit a linear model for price VS sqft_living
lm2 <- lm(price ~ age, data = housing_subset)

# Scatterplot: Price vs. Age of Home
plot(housing_subset$age, housing_subset$price, 
     main = "Price vs. Age of Home", 
     xlab = "Age of Home (years)", 
     ylab = "Price ($)", 
     col = "red", pch = 16)

# Add trendline to the plot
abline(lm2, col = "black", lwd = 2)
legend("topright", legend = paste("Correlation:", round(cor_price_age, 3)), bty = "n")


# Create a combined model and report the results:
# ===============================================

# Subset the original data set to select more variables and create a new data set
housing_6_vars <- c("price","sqft_living","sqft_above", "age", "bathrooms", "view")
housing_subset_6 <- housing_fctrd[housing_6_vars]

# Privew the housing_subset
cat("Housing subset with six variables:")
head(housing_subset_6, 5)

# Create the model
model6 <- lm(price ~ sqft_living + sqft_above + age + bathrooms + view, data=housing_subset_6)
summary(model6)

# Predicted values
# print("fitted values")
fitted_values <- fitted.values(model6) 

# Residuals
# print("residuals")
residuals <- residuals(model6)

# Plot the fitted values and the Q-Q plots
plot(fitted_values, residuals, 
     main = "Residuals against Fitted Values",
     xlab = "Fitted Values", ylab = "Residuals",
     col="red", 
     pch = 19, frame = FALSE)

qqnorm(residuals, pch = 19, col="red", frame = FALSE)
qqline(residuals, col = "blue", lwd = 2)


# Making Predictions Using Model:
# ===============================

# The predicted price for a home with 2150sqft living area, 1050sqft upper level, 15 years old W/3 bathrooms and no view.
newdata1 <- data.frame(sqft_living=2150, sqft_above=1050, age=15, bathrooms=3, view='0')

cat("prediction interval:")
price_prediction_pred_newdata1 <- predict(model6, newdata1, interval="predict", level=0.90) 
round(price_prediction_pred_newdata1, 4)

cat("confidence interval:")
price_prediction_conf_newdata1 <- predict(model6, newdata1, interval="confidence", level=0.90) 
round(price_prediction_conf_newdata1, 4)

# Extract 'fit' value from the matrix
fit_value <- round(price_prediction_conf_newdata1[1, 1], 0) 
cat(paste("The predicted price for a home with 2150sqft living area, 1050sqft upper level, 15 years old W/3 bathrooms and no view is approximately $", fit_value))

cat("\n\n\n")

# The predicted price for a home with 4250sqft living area, 2100sqft upper level, 5 years old W/5 bathrooms and view to a lake.
newdata2 <- data.frame(sqft_living=4250, sqft_above=2100, age=5, bathrooms=5, view='2')

cat("prediction interval:")
price_prediction_pred_newdata2 <- predict(model6, newdata2, interval="predict", level=0.90) 
round(price_prediction_pred_newdata2, 4)

cat("confidence interval:")
price_prediction_conf_newdata2 <- predict(model6, newdata2, interval="confidence", level=0.90) 
round(price_prediction_conf_newdata2, 4)

# Extract 'fit' value from the matrix
fit_value <- round(price_prediction_conf_newdata2[1, 1], 0) 
cat(paste("The predicted price for a home with 4250sqft living area, 2100sqft upper level, 5 years old W/5 bathrooms and view to a lake is approximately $", fit_value))


# Subset the original data set to select some variables and create a new data set
housing_vars_2 <- c("price","school_rating","crime")
housing_subset_2 <- housing_fctrd[housing_vars_2]

# Privew the housing_subset
print("housing_subset_2")
head(housing_subset_2, 5)

# Display the correlation matrix
cat("\nCorrelation Analysis:")
corr_matrix_2 <- cor(housing_subset_2, method = "pearson")
round(corr_matrix_2, 4)
cat("\n")

# Find the correlation betwwen price and average school rating.
cor_price_school <- cor(housing_subset_2$price, housing_subset_2$school_rating, method = "pearson")

# Fit a linear model for price VS school_rating
lm_price_school <- lm(price ~ school_rating, data = housing_subset_2)

# Create scatterplots of price (price) vs. average school rating in the area (school_rating)
plot(housing_subset_2$school_rating, housing_subset_2$price, 
     main = "Price vs. Average School Rating", 
     xlab = "School rating", 
     ylab = "Price ($)", 
     col = "blue", pch = 16)

# Add trendline to the plot
abline(lm_price_school, col = "black", lwd = 2)

# Add correlation to the plot
legend("topleft", legend = paste("Correlation:", round(cor_price_school, 3)), bty = "n")


# Find the correlation betwwen price and crime
cor_price_crime <- cor(housing_subset_2$price, housing_subset_2$crime, method = "pearson")

# Fit a linear model for price VS crime
lm_price_crime <- lm(price ~ crime, data = housing_subset_2)

# Create scatterplots of price (price) vs. average school rating in the area (school_rating)
plot(housing_subset_2$crime, housing_subset_2$price, 
     main = "Price vs. the crime rate", 
     xlab = "Crime Rate (per 100,000 people)", 
     ylab = "Price ($)", 
     col = "blue", pch = 16)

# Add trendline to the plot
abline(lm_price_crime, col = "black", lwd = 2)

# Add correlation to the plot
legend("topright", legend = paste("Correlation:", round(cor_price_crime, 3)), bty = "n")


# Create the second order regression model and print the statistics
model2_prc_schl_crm <- lm(price ~ school_rating + crime + school_rating:crime + I(school_rating^2) + I(crime^2) , data=housing_subset_2)
summary(model2_prc_schl_crm)

# Predicted values
# print("fitted values")
fitted_values_prc_schl_crm <- fitted.values(model2_prc_schl_crm) 

# Residuals
# print("residuals")
residuals_values_prc_schl_crm <- residuals(model2_prc_schl_crm)

# Plot the fitted values and the Q-Q plots
plot(fitted_values_prc_schl_crm, residuals_values_prc_schl_crm, 
     main = "Residuals against Fitted Values",
     xlab = "Fitted Values", ylab = "Residuals",
     col="red", 
     pch = 19, frame = FALSE)

qqnorm(residuals_values_prc_schl_crm, pch = 19, col="red", frame = FALSE)
qqline(residuals_values_prc_schl_crm, col = "blue", lwd = 2)


# Making Predictions Using Model:
# ===============================

# The predicted price for a home with average school rating of 9.80, and a crime rate of 81.02 per 100,000 individuals.
newdata1_prc_schl_crm <- data.frame(school_rating=9.80, crime=81.02)

cat("90% prediction interval:")
price_pred_prc_schl_crm1 <- predict(model2_prc_schl_crm, newdata1_prc_schl_crm, interval="predict", level=0.90) 
round(price_pred_prc_schl_crm1, 4)

cat("90% confidence interval:")
price_conf_prc_schl_crm1 <- predict(model2_prc_schl_crm, newdata1_prc_schl_crm, interval="confidence", level=0.90) 
round(price_conf_prc_schl_crm1, 4)

# Extract 'fit' value from the matrix
fit_value1 <- round(price_conf_prc_schl_crm1[1, 1], 0) 
cat(paste("The predicted price for a home in an area with \naverage school rating of 9.80, and a crime rate of 81.02 is approximately $", fit_value1))

cat("\n\n\n")


# The predicted price for a home with average school rating of 4.28, and a crime rate of 215.50 per 100,000 individuals.
newdata2_prc_schl_crm <- data.frame(school_rating=4.28, crime=215.50)

cat("90% prediction interval:")
price_pred_prc_schl_crm2 <- predict(model2_prc_schl_crm, newdata2_prc_schl_crm, interval="predict", level=0.90) 
round(price_pred_prc_schl_crm2, 4)

cat("90% confidence interval:")
price_conf_prc_schl_crm2 <- predict(model2_prc_schl_crm, newdata2_prc_schl_crm, interval="confidence", level=0.90) 
round(price_conf_prc_schl_crm2, 4)

# Extract 'fit' value from the matrix
fit_value2 <- round(price_conf_prc_schl_crm2[1, 1], 0) 
cat(paste("The predicted price for a home in an area with \naverage school rating of 4.28, and a crime rate of 215.50 is approximately $", fit_value2))


# The first order model for price using average school rating and crime rate including average 
# school rating and crime rate as interaction terms as predictors.

model3_schl_crm_interact <- lm(price ~ school_rating + crime + school_rating:crime, data=housing_subset_2)
summary(model3_schl_crm_interact)

# The second order quadratic model #2 ("model2_prc_schl_crm") VS the first-order linear model with interaction terms ("model3_schl_crm_interact") comparison

# Perform the F-test
anova(model2_prc_schl_crm, model3_schl_crm_interact)