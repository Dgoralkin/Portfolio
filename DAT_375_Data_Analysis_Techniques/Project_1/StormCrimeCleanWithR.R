install.packages("dplyr")
install.packages("readr")
install.packages("stringr")
install.packages("tidyr")
install.packages("outliers")

# Load the packages
library(dplyr)
library(readr)
library(stringr)
library(tidyr)
library(outliers)

MessyDataset <- read_csv('C:/Users/Goral/OneDrive/Desktop/SNHU/Term 12 - 25C3/DAT-375/Module 4/StormCrimesMessy.csv')
head(MessyDataset, 5)

# 1. Removes leading and trailing extra spaces from all character and replaces multiple inner spaces with a single space of a column.
MessyDataset <- MessyDataset %>% mutate(across(where(is.character), ~ str_squish(.)))
head(MessyDataset, 10)
names(MessyDataset)

# 2. Convert columns with numbers stored as text to numeric values.
MessyDataset <- MessyDataset %>%
  mutate(
    ID = as.numeric(ID),
    `Crime Event ID` = as.numeric(`Crime Event ID`),
    `Storm.Event-ID` = as.numeric(`Storm.Event-ID`),
    ZoneCityID = as.numeric(ZoneCityID)
  )
head(MessyDataset, 5)

# Check the class of a specific column (e.g., "Crime Event ID")
class(MessyDataset$ID)


# 3. Remove duplicate data rows
MessyDataset <- MessyDataset %>% distinct()
head(MessyDataset, 5)

# 4. Rename columns for consistency. Remove symbols, spaces, or any not English letter or a digit.
MessyDataset <- MessyDataset %>% rename_all(~ gsub("[^A-Za-z0-9]+", "", .))
head(MessyDataset, 5)

# 5. Handle missing data (e.g., replace NAs with "Missing_Data" or 0)
head(MessyDataset, 7)

MessyDataset <- MessyDataset %>%
  mutate(across(where(is.character), ~ replace_na(.x, "Missing_Data"))) %>%
  mutate(across(where(is.numeric), ~ replace_na(.x, 0)))

head(MessyDataset, 7)

# Remove observations that have any zero causes the row to be dropped
CleanedDataset <- MessyDataset %>%
  filter(if_all(where(is.numeric), ~ . != 0))
  # Or:
  filter(if_all(everything(), ~ . != 0)) # any zero causes the row to be dropped

head(CleanedDataset, 10)

# 6. Output cleaned dataset to a CSV file.
# write_csv(CleanedDataset, "C:/Users/Goral/OneDrive/Desktop/SNHU/Term 12 - 25C3/DAT-375/Module 4/StormCrimesCleaned.csv")

