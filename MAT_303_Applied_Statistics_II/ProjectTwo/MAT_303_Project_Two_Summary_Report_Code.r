
# Load necessary libraries
print("This step will first install three R packages. Please wait until the packages are fully installed.")
print("Once the installation is complete, this step will print 'Installation complete!'")

install.packages("ResourceSelection")
install.packages("pROC")
install.packages("rpart.plot")

print("Installation complete!")

# Load dataset
heart_data <- read.csv(file="C:/Users/Goral/OneDrive/Desktop/SNHU/Term 11 - 25C2/MAT-303/ProjectTwo/heart_disease.csv", header=TRUE, sep=",")

# Converting appropriate variables to factors
heart_data <- within(heart_data, {
   target <- factor(target)
   sex <- factor(sex)
   cp <- factor(cp)
   fbs <- factor(fbs)
   restecg <- factor(restecg)
   exang <- factor(exang)
   slope <- factor(slope)
   ca <- factor(ca)
   thal <- factor(thal)
})

head(heart_data, 10)

print("Number of variables")
ncol(heart_data)

print("Number of rows")
nrow(heart_data)


# Logistic Regression Model_1
# ------------------------------------
cat("Logistic Regression Model_1\n-------------------------------------------------\n")

# Create the complete model
model_1 <- glm(target ~ age + trestbps + thalach + exang, data = heart_data, family = "binomial")

summary(model_1)


# Hosmer-Lemeshow goodness of fit test
# ------------------------------------
cat("Hosmer-Lemeshow goodness of fit test\n-------------------------------------------------\n")

# Load required library
library(ResourceSelection)

hl = hoslem.test(model_1$y, fitted(model_1), g = 50)
hl


# Wald’s test
# ------------------------------------
cat("Walds test with a 5% level of significance\n-------------------------------------------------")

conf_int_1 <- confint.default(model_1, level=0.95)
round(conf_int_1,4)


# Confusion matrix
# ------------------------------------
cat("Confusion matrix\n-------------------------------------------------\n")

# Prepare data for prediction
target_model_data_1 <- heart_data[c('age', 'trestbps', 'thalach', 'exang')]

# Predict probabilities
pred_1 <- predict(model_1, newdata = target_model_data_1, type = 'response')
depvar_pred_1 <- as.factor(ifelse(pred_1 >= 0.5, '1', '0'))

# Create confusion matrix
conf.matrix_1 <- table(heart_data$target, depvar_pred_1)[c('0','1'), c('0','1')]
rownames(conf.matrix_1) <- paste("Actual", rownames(conf.matrix_1), sep = ": default=")
colnames(conf.matrix_1) <- paste("Prediction", colnames(conf.matrix_1), sep = ": default=")

# Print Confusion Matrix
cat("Confusion Matrix for model_1:\n")
print(conf.matrix_1)

# Extract values from the Confusion matrix
TP <- conf.matrix_1["Actual: default=1", "Prediction: default=1"]
TN <- conf.matrix_1["Actual: default=0", "Prediction: default=0"]
FP <- conf.matrix_1["Actual: default=0", "Prediction: default=1"]
FN <- conf.matrix_1["Actual: default=1", "Prediction: default=0"]
cat("\n")

# Print values from the Confusion matrix
print(paste("True Positives: ", TP))
print(paste("True Negatives: ", TN))
print(paste("False Positives: ", FP))
print(paste("False Negatives: ", FN))
print(paste("Total Observations: ", TP + TN + FP + FN))
cat("\n")

# Calculate and Print Accuracy, Precision, and Recall
Accuracy <- round((TP + TN) / (TP + TN + FP + FN), 3)
cat("Accuracy: ", Accuracy, "/", Accuracy*100, "%\n")

Precision <- round(TP / (TP + FP), 3)
cat("Precision: ", Precision, "/", Precision*100, "%\n")

Recall <- round(TP / (TP + FN), 3)
cat("Recall: ", Recall, "/", Recall*100, "%\n")


# Receiver Operating Characteristic (ROC) and (AUC)
# -------------------------------------------------

# Load required library
library(pROC)

cat("Receiver Operating Characteristic (ROC) and (AUC)\n-------------------------------------------------\n")

labels <- heart_data$target
predictions <- model_1$fitted.values

roc_1 <- roc(labels ~ predictions)

print("Area Under the Curve (AUC)")
round(auc(roc_1),4)

print("ROC Curve")
# True Positive Rate (Sensitivity) and False Positive Rate (1 - Specificity)
plot(roc_1, legacy.axes = TRUE)


# Making Predictions Using Model_1)
# -------------------------------------------------
cat("Making Predictions Using Model_1\n-------------------------------------------------\n")

print("Prediction for an individual having heart disease, 50 years old, resting blood pressure of 122, maximum heart rate of 140, and has exercise induced angina.")
newdata1_1 <- data.frame(age=50, trestbps=122, thalach=140, exang="1")
pred1_1 <- predict(model_1, newdata1_1, type='response')
cat()
cat("Probability of heart disease:", round(pred1_1, 4), "or approximately", round(pred1_1, 3)*100, "%\n")
odds1 <- pred1_1 / (1-pred1_1)
cat("Odds to heart disease:", round(odds1, 4))

cat("\n\n")

print("Prediction for an individual having heart disease, 50 years old, resting blood pressure of 130, maximum heart rate of 165, and does not has exercise induced angina.")
newdata1_2 <- data.frame(age=50, trestbps=130, thalach=165, exang="0")
pred1_2 <- predict(model_1, newdata1_2, type='response')
cat()
cat("Probability of heart disease:", round(pred1_2, 4), "or approximately", round(pred1_2, 3)*100, "%\n")
odds2 <- pred1_2 / (1-pred1_2)
cat("Odds to heart disease:", round(odds2, 4))


# Logistic Regression Model_2
# ------------------------------------
cat("Logistic Regression Model_2\n-------------------------------------------------\n")

# Create the complete model for heart disease (target)
# using variables age (age), resting blood pressure (trestbps), maximum heart rate achieved (thalach),
# type of chest pain experienced (cp), quadratic term for age,
# and the interaction term between age and maximum heart rate achieved
model_2 <- glm(target ~ age + trestbps + thalach + cp + age:thalach + I(age^2), data = heart_data, family = "binomial")

summary(model_2)


# Hosmer-Lemeshow goodness of fit test
# ------------------------------------
cat("Hosmer-Lemeshow goodness of fit test\n-------------------------------------------------\n")

# Load required library
library(ResourceSelection)

hl = hoslem.test(model_2$y, fitted(model_2), g=50)
hl


# Wald’s test
# ------------------------------------
cat("Walds test with a 5% level of significance\n-------------------------------------------------")

conf_int_2 <- confint.default(model_2, level=0.95)
round(conf_int_2,4)

# Confusion matrix
# ------------------------------------
cat("Confusion matrix\n-------------------------------------------------\n")

# Prepare data for prediction
target_model_data_2 <- heart_data[c('age', 'trestbps', 'thalach', 'cp')]

# Predict probabilities
pred_2 <- predict(model_2, newdata = target_model_data_2, type = 'response')
depvar_pred_2 <- as.factor(ifelse(pred_2 >= 0.5, '1', '0'))

# Create confusion matrix
conf.matrix_2 <- table(heart_data$target, depvar_pred_2)[c('0','1'), c('0','1')]
rownames(conf.matrix_2) <- paste("Actual", rownames(conf.matrix_2), sep = ": default=")
colnames(conf.matrix_2) <- paste("Prediction", colnames(conf.matrix_2), sep = ": default=")

# Print Confusion Matrix
cat("Confusion Matrix for model_2:\n")
print(conf.matrix_2)

# Extract values from the Confusion matrix
TP <- conf.matrix_2["Actual: default=1", "Prediction: default=1"]
TN <- conf.matrix_2["Actual: default=0", "Prediction: default=0"]
FP <- conf.matrix_2["Actual: default=0", "Prediction: default=1"]
FN <- conf.matrix_2["Actual: default=1", "Prediction: default=0"]
cat("\n")

# Print values from the Confusion matrix
print(paste("True Positives: ", TP))
print(paste("True Negatives: ", TN))
print(paste("False Positives: ", FP))
print(paste("False Negatives: ", FN))
print(paste("Total Observations: ", TP + TN + FP + FN))
cat("\n")

# Calculate and Print Accuracy, Precision, and Recall
Accuracy <- round((TP + TN) / (TP + TN + FP + FN), 3)
cat("Accuracy: ", Accuracy, "/", Accuracy*100, "%\n")

Precision <- round(TP / (TP + FP), 3)
cat("Precision: ", Precision, "/", Precision*100, "%\n")

Recall <- round(TP / (TP + FN), 3)
cat("Recall: ", Recall, "/", Recall*100, "%\n")


# Receiver Operating Characteristic (ROC) and (AUC)
# -------------------------------------------------

# Load required library
library(pROC)

cat("Receiver Operating Characteristic (ROC) and (AUC)\n-------------------------------------------------\n")

labels <- heart_data$target
predictions <- model_2$fitted.values

roc_2 <- roc(labels ~ predictions)

print("Area Under the Curve (AUC)")
round(auc(roc_2),4)

print("ROC Curve")
# True Positive Rate (Sensitivity) and False Positive Rate (1 - Specificity)
plot(roc_2, legacy.axes = TRUE)


# Making Predictions Using Model_2)
# -------------------------------------------------
cat("Making Predictions Using Model_2\n-------------------------------------------------\n")

print("Prediction for an individual having heart disease, 50 years old, resting blood pressure of 115, maximum heart rate of 133, and does not experience chest pain.")
newdata2_1 <- data.frame(age=50, trestbps=115, thalach=133, cp="0")
pred2_1 <- predict(model_2, newdata2_1, type='response')
cat()
cat("Probability of heart disease:", round(pred2_1, 4), "or approximately", round(pred2_1, 3)*100, "%\n")
odds2_1 <- pred2_1 / (1-pred2_1)
cat("Odds to heart disease:", round(odds2_1, 4))

cat("\n\n")

print("Prediction for an individual having heart disease, 50 years old, resting blood pressure of 125, maximum heart rate of 155, and experiences typical angina.")
newdata2_2 <- data.frame(age=50, trestbps=125, thalach=155, cp="1")
pred2_2 <- predict(model_2, newdata2_2, type='response')
cat()
cat("Probability of heart disease:", round(pred2_2, 4), "or approximately", round(pred2_2, 3)*100, "%\n")
odds2_2 <- pred2_2 / (1-pred2_2)
cat("Odds to heart disease:", round(odds2_2, 4))


# Random Forest Classification Model
# ------------------------------------
cat("\tRandom Forest Classification Model\n==================================================\n\n")

vars <- c('target','age','sex', 'cp', 'trestbps', 'chol', 'restecg', 'exang', 'ca')

# Create data set for the Random Forest Classification Model
heart_data_RF1 <- heart_data[vars]

print("head")
head(heart_data_RF1, 5)

cat("Number of Rows:", nrow(heart_data_RF1), "\t\tNumber of Columns:", ncol(heart_data_RF1), "\n\n")


# Splitting Data into Training and Testing Sets

set.seed(6522048)

# Partition the data set into training and testing data sets by the 85%/15% ratio.
samp.size <- floor(0.85*nrow(heart_data_RF1))

# Define training set
train_ind <- sample(seq_len(nrow(heart_data_RF1)), size = samp.size)
train.data <- heart_data_RF1[train_ind, ]
cat("Number of rows for the training set:", nrow(train.data), "\n")

# Define testing set 
test.data <- heart_data_RF1[-train_ind, ]
cat("Number of rows for the testing set:", nrow(test.data), "\n")


# Build the Random Forest Classification Model.

library(randomForest)
set.seed(6522048)

# Grow a random forest with 150 trees and 8 randomly chosen attributes
model_RFC_150 <- randomForest(target ~ age + sex + cp + trestbps + chol + restecg + exang + ca, data=train.data, ntree = 150)
print(model_RFC_150)


set.seed(6522048)

cat('Confusion Matrix: TRAINING set based on random forest model built using 150 trees
====================================================================================')
train.data.predict <- predict(model_RFC_150, train.data, type = "class")

# Construct the confusion matrix for TRAINING
conf.matrix_RFC_150 <- table(train.data$target, train.data.predict)[,c('0','1')]
rownames(conf.matrix_RFC_150) <- paste("Actual", rownames(conf.matrix_RFC_150), sep = ": ")
colnames(conf.matrix_RFC_150) <- paste("Prediction", colnames(conf.matrix_RFC_150), sep = ": ")

# Print formatted confusion matrix
format(conf.matrix_RFC_150, justify = "centre", digit=2)


cat('Confusion Matrix: TESTING set based on random forest model built using 150 trees
====================================================================================')
test.data.predict <- predict(model_RFC_150, test.data, type = "class")

# Construct the confusion matrix for TESTING
conf.matrix_RFC_150 <- table(test.data$target, test.data.predict)[,c('0','1')]
rownames(conf.matrix_RFC_150) <- paste("Actual", rownames(conf.matrix_RFC_150), sep = ": ")
colnames(conf.matrix_RFC_150) <- paste("Prediction", colnames(conf.matrix_RFC_150), sep = ": ")

# Print formatted confusion matrix
format(conf.matrix_RFC_150, justify = "centre", digit=2)

# Extract values from the Confusion matrix for TRAINING
TP <- conf.matrix_RFC_150["Actual: 1", "Prediction: 1"]
TN <- conf.matrix_RFC_150["Actual: 0", "Prediction: 0"]
FP <- conf.matrix_RFC_150["Actual: 0", "Prediction: 1"]
FN <- conf.matrix_RFC_150["Actual: 1", "Prediction: 0"]
cat("\n")

# Print values from the Confusion matrix
print(paste("True Positives: ", TP))
print(paste("True Negatives: ", TN))
print(paste("False Positives: ", FP))
print(paste("False Negatives: ", FN))
print(paste("Total Observations: ", TP + TN + FP + FN))
cat("\n")

# Calculate and Print Accuracy, Precision, and Recall
Accuracy <- round((TP + TN) / (TP + TN + FP + FN), 3)
cat("Accuracy: ", Accuracy, "/", Accuracy*100, "%\n")

Precision <- round(TP / (TP + FP), 3)
cat("Precision: ", Precision, "/", Precision*100, "%\n")

Recall <- round(TP / (TP + FN), 3)
cat("Recall: ", Recall, "/", Recall*100, "%\n")


# Graphing the Training and Testing Error Against the Number of Trees

set.seed(6522048)
library(randomForest)

print("Graph of the training and testing error against the number of trees using a classification random forest model for the presence of heart disease (target) using variables age, sex, chest pain type, resting blood pressure, cholesterol measurement, resting electrocardiographic measurement, exercise-induced angina, and number of major vessels (ca).")


# checking
#=====================================================================
train = c()
test = c()
trees = c()

for(i in seq(from=1, to=150, by=1)) {
    #print(i)
    
    trees <- c(trees, i)
    
    model_RFC_150 <- randomForest(target ~ age + sex + cp + trestbps + chol + restecg + exang + ca, data=train.data, ntree = i)
    
    train.data.predict <- predict(model_RFC_150, train.data, type = "class")
    conf.matrix1 <- table(train.data$target, train.data.predict)
    train_error = 1-(sum(diag(conf.matrix1)))/sum(conf.matrix1)
    train <- c(train, train_error)
    
    test.data.predict <- predict(model_RFC_150, test.data, type = "class")
    conf.matrix2 <- table(test.data$target, test.data.predict)
    test_error = 1-(sum(diag(conf.matrix2)))/sum(conf.matrix2)
    test <- c(test, test_error)
}

plot(trees, train,type = "l",ylim=c(0,0.5),col = "red", xlab = "Number of Trees", ylab = "Classification Error")
lines(test, type = "l", col = "blue")
legend('topright',legend = c('training set','testing set'), col = c("red","blue"), lwd = 2 )


# Grow a random forest with 50 trees and 8 randomly chosen attributes
set.seed(6522048)
model_RFC_50 <- randomForest(target ~ age + sex + cp + trestbps + chol + restecg + exang + ca, data=train.data, ntree = 50)
print(model_RFC_50)


cat('Confusion Matrix: TRAINING set based on random forest model built using 50 trees
====================================================================================')
set.seed(6522048)

train.data.predict <- predict(model_RFC_50, train.data, type = "class")

# Construct the confusion matrix for TRAINING
conf.matrix_RFC_50 <- table(train.data$target, train.data.predict)[,c('0','1')]
rownames(conf.matrix_RFC_50) <- paste("Actual", rownames(conf.matrix_RFC_50), sep = ": ")
colnames(conf.matrix_RFC_50) <- paste("Prediction", colnames(conf.matrix_RFC_50), sep = ": ")

# Print formatted confusion matrix
format(conf.matrix_RFC_50, justify = "centre", digit=2)

# Extract values from the Confusion matrix for TRAINING
TP <- conf.matrix_RFC_50["Actual: 1", "Prediction: 1"]
TN <- conf.matrix_RFC_50["Actual: 0", "Prediction: 0"]
FP <- conf.matrix_RFC_50["Actual: 0", "Prediction: 1"]
FN <- conf.matrix_RFC_50["Actual: 1", "Prediction: 0"]
cat("\n")

# Print values from the Confusion matrix
print(paste("True Positives: ", TP))
print(paste("True Negatives: ", TN))
print(paste("False Positives: ", FP))
print(paste("False Negatives: ", FN))
print(paste("Total Observations: ", TP + TN + FP + FN))
cat("\n")

# Calculate and Print Accuracy, Precision, and Recall
Accuracy <- round((TP + TN) / (TP + TN + FP + FN), 3)
cat("Accuracy: ", Accuracy, "/", Accuracy*100, "%\n")

Precision <- round(TP / (TP + FP), 3)
cat("Precision: ", Precision, "/", Precision*100, "%\n")

Recall <- round(TP / (TP + FN), 3)
cat("Recall: ", Recall, "/", Recall*100, "%\n")


cat('Confusion Matrix: TESTING set based on random forest model built using 50 trees
====================================================================================')
test.data.predict <- predict(model_RFC_50, test.data, type = "class")

# Construct the confusion matrix for TESTING
conf.matrix_RFC_50 <- table(test.data$target, test.data.predict)[,c('0','1')]
rownames(conf.matrix_RFC_50) <- paste("Actual", rownames(conf.matrix_RFC_50), sep = ": ")
colnames(conf.matrix_RFC_50) <- paste("Prediction", colnames(conf.matrix_RFC_50), sep = ": ")

# Print formatted confusion matrix
format(conf.matrix_RFC_50, justify = "centre", digit=2)

# Extract values from the Confusion matrix for TRAINING
TP <- conf.matrix_RFC_50["Actual: 1", "Prediction: 1"]
TN <- conf.matrix_RFC_50["Actual: 0", "Prediction: 0"]
FP <- conf.matrix_RFC_50["Actual: 0", "Prediction: 1"]
FN <- conf.matrix_RFC_50["Actual: 1", "Prediction: 0"]
cat("\n")

# Print values from the Confusion matrix
print(paste("True Positives: ", TP))
print(paste("True Negatives: ", TN))
print(paste("False Positives: ", FP))
print(paste("False Negatives: ", FN))
print(paste("Total Observations: ", TP + TN + FP + FN))
cat("\n")

# Calculate and Print Accuracy, Precision, and Recall
Accuracy <- round((TP + TN) / (TP + TN + FP + FN), 3)
cat("Accuracy: ", Accuracy, "/", Accuracy*100, "%\n")

Precision <- round(TP / (TP + FP), 3)
cat("Precision: ", Precision, "/", Precision*100, "%\n")

Recall <- round(TP / (TP + FN), 3)
cat("Recall: ", Recall, "/", Recall*100, "%\n")


# Random Forest Regression Model
# ------------------------------------
cat("\tRandom Forest Regression Model\n==================================================\n\n")
set.seed(6522048)

vars <- c('thalach','age','sex', 'cp', 'trestbps', 'chol', 'restecg', 'exang', 'ca')

# Create data set for the Random Forest Classification Model
heart_data_RF2 <- heart_data[vars]

print("head")
head(heart_data_RF2, 5)

cat("Number of Rows:", nrow(heart_data_RF2), "\t\tNumber of Columns:", ncol(heart_data_RF2), "\n\n")


# Splitting Data into Training and Testing Sets

set.seed(6522048)

# Partition the data set into training and testing data sets by the 80%/20% ratio.
samp.size <- floor(0.8*nrow(heart_data_RF2))

# Define training set
train_ind <- sample(seq_len(nrow(heart_data_RF2)), size = samp.size)
train.data <- heart_data_RF2[train_ind, ]
cat("Number of rows for the training set:", nrow(train.data), "\n")

# Define testing set 
test.data <- heart_data_RF2[-train_ind, ]
cat("Number of rows for the testing set:", nrow(test.data), "\n")


# Graphing the Training and Testing Error Against the Number of Trees

set.seed(6522048)
library(randomForest)

# Root mean squared error
RMSE = function(pred, obs) {
    return(sqrt( sum( (pred - obs)^2 )/length(pred) ) )
}

print("Graph of the training and testing error against the number of trees using a random forest regression model for maximum heart rate achieved using variables age, sex, chest pain type, resting blood pressure, cholesterol measurement, resting electrocardiographic measurement, exercise-induced angina, and number of major vessels.")

# checking
#=====================================================================
train = c()
test = c()
trees = c()

for(i in seq(from=1, to=80, by=1)) {
    trees <- c(trees, i)
    model_rf80 <- randomForest(thalach ~ age + sex + cp + trestbps + chol + restecg + exang + ca, data=train.data, ntree = i)
    
    pred <- predict(model_rf80, newdata=train.data, type='response')
    rmse_train <-  RMSE(pred, train.data$thalach)
    train <- c(train, rmse_train)
    
    pred <- predict(model_rf80, newdata=test.data, type='response')
     rmse_test <-  RMSE(pred, test.data$thalach)
    test <- c(test, rmse_test)
}
 
plot(trees, train,type = "l",ylim=c(10,30),col = "red", xlab = "Number of Trees", ylab = "Root Mean Squared Error")
lines(test, type = "l", col = "blue")
legend('topright',legend = c('training set','testing set'), col = c("red","blue"), lwd = 2 )


# Grow a random forest with 20 trees and 8 randomly chosen attributes
set.seed(6522048)

# Root mean squared error
RMSE = function(pred, obs) {
    return(sqrt( sum( (pred - obs)^2 )/length(pred) ) )
}

model_rf20 <- randomForest(thalach ~ age + sex + cp + trestbps + chol + restecg + exang + ca, data=train.data, ntree = 20)
print(model_rf20)

print("==========================================================================================")
print('Root Mean Squared Error: TRAINING set based on random forest model built using 20 trees')
pred <- predict(model_rf20, newdata=train.data, type='response')
round(RMSE(pred, train.data$thalach), 4)


print("==========================================================================================")
print('Root Mean Squared Error: TESTING set based on random forest model built using 20 trees')
pred <- predict(model_rf20, newdata=test.data, type='response')
round(RMSE(pred, test.data$thalach), 4)
