### Script which contains entire implementation of 1. seminary work for 
### AI course
### Authors: Luka Šveigl, Nejc Vrèon Zupan



### Load needed datasets: 

sveigl <- "G:/Documents/FRI/2. letnik/Umetna inteligenca/Seminarska naloga 1"
#vrcon <- ""

# Set working directory - change as needed
setwd(sveigl)
#setwd(vrcon)

# Load test dataset
test <- read.table("testnaSem1.txt", header = T, sep = ",", stringsAsFactors = T)

# Load training dataset
train <- read.table("ucnaSem1.txt", header = T, sep = ",", stringsAsFactors = T)



## Cleanup of data

# Transform datum column into type Date
test$datum <- as.Date(test$datum)
train$datum <- as.Date(train$datum)

# Order dataframe by building and date
test <- test[order(test$stavba, test$datum), ]
train <- train[order(train$stavba, train$datum), ]






### Modification of data

## Create a new attribute called letni_Cas based on datum attribute,
## using the meteorological definition of a season 
## (ie. each season spans 3 full months)


## Create attribute letni_cas for test dataset

# Extract months from test dataframe
test$mesec <- as.numeric(format(test$datum, "%m"))

# Create new column of meteorological seasons
test$letni_cas <- ""

# Months: December, January, February
test$letni_cas[test$mesec < 3 | test$mesec > 11] <- "zima"

# Months: March, April, May
test$letni_cas[test$mesec > 2 & test$mesec < 6] <- "pomlad"

# Months: June, July, August
test$letni_cas[test$mesec > 5 & test$mesec < 9] <- "poletje"

# Months: September, October, November
test$letni_cas[test$mesec > 8 & test$mesec < 12] <- "jesen"

# Factorize attribute letni_cas
test$letni_cas <- factor(test$letni_cas)


## Create attribute letni_cas for train dataset

# Extract months from test dataframe
train$mesec <- as.numeric(format(train$datum, "%m"))

# Create new column of meteorological seasons
train$letni_cas <- ""

# Months: December, January, February
train$letni_cas[train$mesec < 3 | train$mesec > 11] <- "zima"

# Months: March, April, May
train$letni_cas[train$mesec > 2 & train$mesec < 6] <- "pomlad"

# Months: June, July, August
train$letni_cas[train$mesec > 5 & train$mesec < 9] <- "poletje"

# Months: September, October, November
train$letni_cas[train$mesec > 8 & train$mesec < 12] <- "jesen"

# Factorize attribute letni_cas
train$letni_cas <- factor(train$letni_cas)



## Create attribute vikend from datum attribute, which contains values 1 if weekend
## and 0 if not weekend


## Create attribute vikend for test dataframe

# Load library timeDate, which contains function isWeekday
# Install this library via install.packages("timeDate")
library(timeDate)

# Create new column called vikend
test$vikend <- 0

# If not weekday, set value to 1
test$vikend[!isWeekday(test$datum)] <- 1


## Create attribute vikend for train dataframe

# Create new column called vikend
train$vikend <- 0

# If not weekday, set value to 1
train$vikend[!isWeekday(train$datum)] <- 1


## Create attribute poraba_prteden, which contains data of energy consumption
## from previous week for each building


# Load library zoo, which contains function rollmean
# Install this library via install.packages("zoo")
library(zoo)

## Create attribute poraba_prteden for test dataframe

# Create temporary variable which holds average energy usage for previous week.
# Where usage couldn't be calculated (less than 7 previous records) is replaced
# by NA values.
tmp_poraba <- aggregate(test$poraba, by = list(test$stavba), 
                        rollmean, 7, na.pad = TRUE, align = "right")

# Create temporary column called poraba_prteden
test$poraba_prteden <- 0

# Loop through indices of result and add values to correct places in dataframe
for (i in c(1:length(tmp_poraba$x))) {
  test$poraba_prteden[test$stavba == i * 2] = tmp_poraba$x[[i]]
}


## Create attribute poraba_prteden for train dataframe

# Create temporary variable which holds average energy usage for previous week.
# Where usage couldn't be calculated (less than 7 previous records) is replaced
# by NA values.
tmp_poraba <- aggregate(train$poraba, by = list(train$stavba), 
                        rollmean, 7, na.pad = TRUE, align = "right")

# Create temporary column called poraba_prteden
train$poraba_prteden <- 0

# Loop through indices of result and add values to correct places in dataframe
for (i in c(1:length(tmp_poraba$x))) {
  train$poraba_prteden[train$stavba == (i * 2) - 1] = tmp_poraba$x[[i]]
}


## Remove rows with NA values
test <- na.omit(test)
train <- na.omit(train)

## Remove unneeded columns datum and stavba
test <- test[, -c(1, 3)]
train <- train[, -c(1, 3)]


### TODO: ADD CUSTOMIZATION
### Visualisation 


## Graph of distribution of usage for each dataframe

# Graph of distribution of usage for dataframe test
boxplot(test$poraba, main = "Porazdelitev porabe (test)", ylab = "Poraba")

# Graph of distribution of usage for dataframe train
boxplot(train$poraba, main = "Porazdelitev porabe (train)", ylab = "Poraba")


## Graph of average usage per meteorological season for each dataframe

# Get average usages for each season
averages <- aggregate(test$poraba, by = list(test$letni_cas), mean)

# Plot average usage per season
barplot(averages$x, names = unique(averages$Group.1),
        main = "Povpreèna poraba glede na letni èas (test)", 
        xlab = "Letni èas", ylab = "Povpreèna poraba",
        ylim = c(0,200))


# Get average usages for each season
averages <- aggregate(train$poraba, by = list(train$letni_cas), mean)

# Plot average usage per season
barplot(averages$x, names = unique(averages$Group.1),
        main = "Povpreèna poraba glede na letni èas (train)", 
        xlab = "Letni èas", ylab = "Povpreèna poraba",
        ylim = c(0,300))


## Graph of average usage per region

# Get average usages for each region
averages <- aggregate(test$poraba, by = list(test$regija), mean)

# Plot average usage for each region
barplot(averages$x, names = unique(averages$Group.1),
        main = "Povpreèna poraba glede na regijo (test)", 
        xlab = "Regija", ylab = "Povpreèna poraba",
        ylim = c(0,200))


# Get average usages for each region
averages <- aggregate(train$poraba, by = list(train$regija), mean)

# Plot average usage for each region
barplot(averages$x, names = unique(averages$Group.1),
        main = "Povpreèna poraba glede na regijo (train)", 
        xlab = "Regija", ylab = "Povpreèna poraba",
        ylim = c(0,300))


## Graph of average usage per building type

# Get average usages for each building type
averages <- aggregate(test$poraba, by = list(test$namembnost), mean)

# Plot average usage for each building type
barplot(averages$x, names = unique(averages$Group.1),
        main = "Povpreèna poraba glede na namembnost stavbe (test)", 
        xlab = "Namembnost stavbe", ylab = "Povpreèna poraba",
        ylim = c(0,400))


# Get average usages for each building type
averages <- aggregate(train$poraba, by = list(train$namembnost), mean)

# Plot average usage for each building type
barplot(averages$x, names = unique(averages$Group.1),
        main = "Povpreèna poraba glede na namembnost stavbe (train)", 
        xlab = "Namembnost stavbe", ylab = "Povpreèna poraba",
        ylim = c(0,400))


## Graph of poraba_prteden distribution

hist(test$poraba_prteden, main = "Porazdelitev povpreène porabe v enem tednu (test)", xlab = "Povpreèna poraba")  
hist(train$poraba_prteden, main = "Porazdelitev povpreène porabe v enem tednu (train)", xlab = "Povpreèna poraba")






### Attribute evaluation

# Install libraries for classification models via 
# install.packages(c("CORElearn", "e1071", "randomForest", "kernlab", "nnet"))
library(CORElearn)

## Evaluate attributes based on target attribute namembnost - classification

# Get evaluated attributes using different evaluation methods
# Use 3 shortsighted methods and 2 not-shortsighted methods

# Shortsighted methods
InfGain_evaluated_type <- sort(attrEval(namembnost ~ ., train, "InfGain"), decreasing = TRUE)
Gini_evaluated_type <- sort(attrEval(namembnost ~ ., train, "Gini"), decreasing = TRUE)
MDL_evaluated_type <- sort(attrEval(namembnost ~ ., train, "MDL"), decreasing = TRUE)

# Not-shortsighted methods
Relief_evaluated_type <- sort(attrEval(namembnost ~ ., train, "Relief"), decreasing = TRUE)
ReliefF_evaluated_type <- sort(attrEval(namembnost ~ ., train, "ReliefFequalK"), decreasing = TRUE)

# Get some of the best attributes according to non-shortsighted methods
names_reliefF_type <- names(which(ReliefF_evaluated_type > 0.1))
names_relief_type <- names(which(Relief_evaluated_type > 0.01))

# Get some of the best attributes according to shortsighted methods
names_infGain_type <- names(which(InfGain_evaluated_type > 0.05))
names_MDL_type <- names(which(MDL_evaluated_type > 0.05))
names_Gini_type <- names(which(Gini_evaluated_type > 0.01))


## Evaluate attributes based on target attribute poraba - regression

# Get evaluated attributes using different evaluation methods
MSE_evaluated_usage <- sort(attrEval(poraba ~ ., train, "MSEofMean"), decreasing = TRUE)
ReliefF_evaluated_usage <- sort(attrEval(poraba ~ ., train, "RReliefFexpRank"), decreasing = TRUE)
RFM_evaluated_usage <- sort(attrEval(poraba ~ ., train, "RReliefFwithMSE"), decreasing = TRUE)

# Get some of the best attributes
names_MSE_usage <- names(which(MSE_evaluated_usage > -50000))
names_reliefF_usage <- names(which(ReliefF_evaluated_usage > 0.01))
names_RFM_usage <- names(which(RFM_evaluated_usage > 0.1))






### Prediction models

# Load library rpart which contains models for prediction
library(rpart)

# Load library rpart.plot which contains functions to plot models
# Install this library via install.packages("rpart.plot")
library(rpart.plot)


# Load functions script
source("functions.R")



## Classification models

# Decision tree - all attributes
dt_all_attributes <- rpart(namembnost ~ ., data = train)


# Naive Bayes - all attributes

nb_all_attributes <- CoreModel(namembnost ~ ., data = train, model = "bayes")


# K-closest neighbors - all attributes

knn_all_attributes5 <- CoreModel(namembnost ~ ., data = train, model = "knn", kInNN = 5)
knn_all_attributes10 <- CoreModel(namembnost ~ ., data = train, model = "knn", kInNN = 10)
knn_all_attributes20 <- CoreModel(namembnost ~ ., data = train, model = "knn", kInNN = 20)


# Decision tree - shortsighted method attributes

dt_Gini <- rpart(namembnost ~ 
                 povrsina + leto_izgradnje + regija + poraba_prteden + poraba, 
                 data = train)

dt_infGain <- rpart(namembnost ~ 
                    povrsina + regija + leto_izgradnje + temp_zraka + poraba_prteden,
                    data = train)

dt_MDL <- rpart(namembnost ~ 
                  povrsina + regija + leto_izgradnje + temp_zraka + poraba_prteden, 
                data = train)


# Decision tree - non-shortsighted method attributes

dt_relief <- rpart(namembnost ~ 
                   poraba_prteden + poraba + povrsina + leto_izgradnje, 
                   data = train)

dt_reliefF <- rpart(namembnost ~ 
                    leto_izgradnje + povrsina + poraba_prteden + poraba + regija,
                    data = train)



# Naive Bayes - shortsighted method attributes

nb_Gini <- CoreModel(namembnost ~
                       povrsina + leto_izgradnje + regija + poraba_prteden + poraba, 
                     data = train, model = "bayes")

nb_infGain <- CoreModel(namembnost ~
                          povrsina + regija + leto_izgradnje + temp_zraka + poraba_prteden, 
                        data = train, model = "bayes")

nb_MDL <- CoreModel(namembnost ~
                      povrsina + regija + leto_izgradnje + temp_zraka + poraba_prteden, 
                    data = train, model = "bayes")

# Naive Bayes - non-shortsighted method attributes

nb_relief <- CoreModel(namembnost ~
                         poraba_prteden + poraba + povrsina + leto_izgradnje, 
                       data = train, model = "bayes")

nb_reliefF <- CoreModel(namembnost ~
                          leto_izgradnje + povrsina + poraba_prteden + poraba + regija, 
                        data = train, model = "bayes")



# K-closest neighbors - shortsighted method attributes

knn_Gini5 <- CoreModel(namembnost ~ 
                         povrsina + leto_izgradnje + regija + poraba_prteden + poraba, 
                       data = train, model = "knn", kInNN = 5)

knn_Gini10 <- CoreModel(namembnost ~ 
                          povrsina + leto_izgradnje + regija + poraba_prteden + poraba,
                        data = train, model = "knn", kInNN = 10)

knn_Gini20 <- CoreModel(namembnost ~ 
                          povrsina + leto_izgradnje + regija + poraba_prteden + poraba,
                        data = train, model = "knn", kInNN = 20)



knn_infGain5 <- CoreModel(namembnost ~ 
                          ppovrsina + regija + leto_izgradnje + temp_zraka + poraba_prteden, 
                          data = train, model = "knn", kInNN = 5)

knn_infGain10 <- CoreModel(namembnost ~ 
                             povrsina + regija + leto_izgradnje + temp_zraka + poraba_prteden,
                           data = train, model = "knn", kInNN = 10)

knn_infGain20 <- CoreModel(namembnost ~ 
                             povrsina + regija + leto_izgradnje + temp_zraka + poraba_prteden,
                           data = train, model = "knn", kInNN = 20)


knn_MDL5 <- CoreModel(namembnost ~ 
                        povrsina + regija + leto_izgradnje + temp_zraka + poraba_prteden, 
                      data = train, model = "knn", kInNN = 5)

knn_MDL10 <- CoreModel(namembnost ~ 
                         povrsina + regija + leto_izgradnje + temp_zraka + poraba_prteden,
                       data = train, model = "knn", kInNN = 10)

knn_MDL20 <- CoreModel(namembnost ~ 
                         povrsina + regija + leto_izgradnje + temp_zraka + poraba_prteden,
                       data = train, model = "knn", kInNN = 20)


# K-closest neighbors - non-shortsighted method attributes

knn_relief5 <- CoreModel(namembnost ~ 
                           poraba_prteden + poraba + povrsina + leto_izgradnje,  
                         data = train, model = "knn", kInNN = 5)

knn_relief10 <- CoreModel(namembnost ~ 
                            poraba_prteden + poraba + povrsina + leto_izgradnje, 
                          data = train, model = "knn", kInNN = 10)

knn_relief20 <- CoreModel(namembnost ~ 
                            poraba_prteden + poraba + povrsina + leto_izgradnje, 
                          data = train, model = "knn", kInNN = 20)



knn_reliefF5 <- CoreModel(namembnost ~ 
                            leto_izgradnje + povrsina + poraba_prteden + poraba + regija,  
                          data = train, model = "knn", kInNN = 5)

knn_reliefF10 <- CoreModel(namembnost ~ 
                             leto_izgradnje + povrsina + poraba_prteden + poraba + regija,
                           data = train, model = "knn", kInNN = 10)

knn_reliefF20 <- CoreModel(namembnost ~ 
                             leto_izgradnje + povrsina + poraba_prteden + poraba + regija, 
                           data = train, model = "knn", kInNN = 20)


## Classification models - accuracy calculations

observed <- test$namembnost

predicted <- predict(dt_all_attributes, test, type = "class")
dta_CA <- CA(observed, predicted)

predicted <- predict(dt_Gini, test, type = "class")
dgi_CA <- CA(observed, predicted)

predicted <- predict(dt_infGain, test, type = "class")
dig_CA <- CA(observed, predicted)

predicted <- predict(dt_MDL, test, type = "class")
dMD_CA <- CA(observed, predicted)

predicted <- predict(dt_relief, test, type = "class")
dr_CA <- CA(observed, predicted)

predicted <- predict(dt_reliefF, test, type = "class")
drf_CA <- CA(observed, predicted)



predicted <- predict(nb_all_attributes, test, type = "class")
nba_CA <- CA(observed, predicted)

predicted <- predict(nb_Gini, test, type = "class")
nbg_CA <- CA(observed, predicted)

predicted <- predict(nb_infGain, test, type = "class")
nbi_CA <- CA(observed, predicted)

predicted <- predict(nb_MDL, test, type = "class")
nbM_CA <- CA(observed, predicted)

predicted <- predict(nb_relief, test, type = "class")
nbr_CA <- CA(observed, predicted)

predicted <- predict(nb_reliefF, test, type = "class")
nbf_CA <- CA(observed, predicted)



predicted <- predict(knn_all_attributes5, test, type = "class")
k5a_CA <- CA(observed, predicted)

predicted <- predict(knn_all_attributes10, test, type = "class")
k10a_CA <- CA(observed, predicted)

predicted <- predict(knn_all_attributes20, test, type = "class")
k20a_CA <- CA(observed, predicted)


predicted <- predict(knn_Gini5, test, type = "class")
k5g_CA <- CA(observed, predicted)

predicted <- predict(knn_Gini10, test, type = "class")
k10g_CA <- CA(observed, predicted)

predicted <- predict(knn_Gini20, test, type = "class")
k20g_CA <- CA(observed, predicted)


predicted <- predict(knn_infGain5, test, type = "class")
k5i_CA <- CA(observed, predicted)

predicted <- predict(knn_infGain10, test, type = "class")
k10i_CA <- CA(observed, predicted)

predicted <- predict(knn_infGain20, test, type = "class")
k20i_CA <- CA(observed, predicted)


predicted <- predict(knn_MDL5, test, type = "class")
k5M_CA <- CA(observed, predicted)

predicted <- predict(knn_MDL10, test, type = "class")
k10M_CA <- CA(observed, predicted)

predicted <- predict(knn_MDL20, test, type = "class")
k20M_CA <- CA(observed, predicted)


predicted <- predict(knn_relief5, test, type = "class")
k5r_CA <- CA(observed, predicted)

predicted <- predict(knn_relief10, test, type = "class")
k10r_CA <- CA(observed, predicted)

predicted <- predict(knn_relief20, test, type = "class")
k20r_CA <- CA(observed, predicted)


predicted <- predict(knn_reliefF5, test, type = "class")
k5f_CA <- CA(observed, predicted)

predicted <- predict(knn_reliefF10, test, type = "class")
k10f_CA <- CA(observed, predicted)

predicted <- predict(knn_reliefF20, test, type = "class")
k20f_CA <- CA(observed, predicted)

# Create dataframe from values

# Create empty dataframe

rm(CA_df)

CA_df <- data.frame(ClA=character())

CA_df <- rbind(t(dta_CA), CA_df)
CA_df <- rbind(t(dgi_CA), CA_df)
CA_df <- rbind(t(dig_CA), CA_df)
CA_df <- rbind(t(dMD_CA), CA_df)
CA_df <- rbind(t(dr_CA), CA_df)
CA_df <- rbind(t(drf_CA), CA_df)

CA_df <- rbind(t(nba_CA), CA_df)
CA_df <- rbind(t(nbf_CA), CA_df)
CA_df <- rbind(t(nbg_CA), CA_df)
CA_df <- rbind(t(nbi_CA), CA_df)
CA_df <- rbind(t(nbM_CA), CA_df)
CA_df <- rbind(t(nbr_CA), CA_df)

CA_df <- rbind(t(k5a_CA), CA_df)
CA_df <- rbind(t(k5f_CA), CA_df)
CA_df <- rbind(t(k5r_CA), CA_df)
CA_df <- rbind(t(k5g_CA), CA_df)
CA_df <- rbind(t(k5i_CA), CA_df)
CA_df <- rbind(t(k5M_CA), CA_df)

CA_df <- rbind(t(k10a_CA), CA_df)
CA_df <- rbind(t(k10f_CA), CA_df)
CA_df <- rbind(t(k10r_CA), CA_df)
CA_df <- rbind(t(k10g_CA), CA_df)
CA_df <- rbind(t(k10i_CA), CA_df)
CA_df <- rbind(t(k10M_CA), CA_df)

CA_df <- rbind(t(k20a_CA), CA_df)
CA_df <- rbind(t(k20f_CA), CA_df)
CA_df <- rbind(t(k20r_CA), CA_df)
CA_df <- rbind(t(k20g_CA), CA_df)
CA_df <- rbind(t(k20i_CA), CA_df)
CA_df <- rbind(t(k20M_CA), CA_df)

row.names(CA_df) <- c("dta", "dgi", "dig", "dMD", "dr", "drf",
                      "nba", "nbf", "dbg", "dbi", "dbM", "dbr",
                      "k5a", "k5f", "k5r", "k5g", "k5i", "k5M",
                      "k10a", "k10f", "k10r", "k10g", "k10i", "k10M",
                      "k20a", "k20f", "k20r", "k20g", "k20i", "k20M")

colnames(CA_df) <- "ClA"

CA_df <- CA_df[order(-CA_df$ClA), , drop = FALSE]



## Regression models

# Linear regression - all attributes

lr_all_attributes <- lm(poraba ~ ., train)


# Regression tree - all attributes

# Get big regression tree (cp = 0)
rt_all_attributes <- rpart(poraba ~ ., train, cp = 0)

tab <-printcp(rt_all_attributes)

# Get cp that is equal to minimal error of internal cross check
row <- which.min(tab[, "xerror"])
th <- mean(c(tab[row, "CP"], tab[row - 1, "CP"]))

# Prune tree with new cp value
rt_all_attributes <- prune(rt_all_attributes, cp = th)


# K-closest neighbors - all attributes

# Load library kknn, which containts functions for K-closest neighbors models
# Install via install.packages("kknn")

library(kknn)

train$poraba_prteden[is.na(train$poraba_prteden)] <- 0
test$poraba_prteden[is.na(test$poraba_prteden)] <- 0

kn5_all_attributes <- kknn(poraba ~ ., train, test, k = 5)
kn10_all_attributes <- kknn(poraba ~ ., train, test, k = 10)
kn20_all_attributes <- kknn(poraba ~ ., train, test, k = 20)

train$poraba_prteden[train$poraba_prteden == 0] <- NA
test$poraba_prteden[test$poraba_prteden == 0] <- NA

# Linear regression - specific attributes

lr_MSE <- lm(poraba ~ poraba_prteden + povrsina, train)
lr_rF <- lm(poraba ~ poraba_prteden + povrsina + leto_izgradnje, train)
lr_RFM <- lm(poraba ~ poraba_prteden + povrsina + leto_izgradnje, train)


# Regression tree - specific attributes

rt_MSE <- rpart(poraba ~ poraba_prteden + povrsina, train, cp = 0)

tab <-printcp(rt_MSE)

# Get cp that is equal to minimal error of internal cross check
row <- which.min(tab[, "xerror"])
th <- mean(c(tab[row, "CP"], tab[row - 1, "CP"]))

# Prune tree with new cp value
rt_MSE <- prune(rt_MSE, cp = th)


rt_rF <- rpart(poraba ~ poraba_prteden + povrsina + leto_izgradnje, train, cp = 0)

tab <-printcp(rt_rF)

# Get cp that is equal to minimal error of internal cross check
row <- which.min(tab[, "xerror"])
th <- mean(c(tab[row, "CP"], tab[row - 1, "CP"]))

# Prune tree with new cp value
rt_rF <- prune(rt_rF, cp = th)


rt_RFM <- rpart(poraba ~  poraba_prteden + povrsina + leto_izgradnje, train, cp = 0)

tab <-printcp(rt_RFM)

# Get cp that is equal to minimal error of internal cross check
row <- which.min(tab[, "xerror"])
th <- mean(c(tab[row, "CP"], tab[row - 1, "CP"]))

# Prune tree with new cp value
rt_RFM <- prune(rt_RFM, cp = th)


# K-closest neighbors - specific attributes

train$poraba_prteden[is.na(train$poraba_prteden)] <- 0
test$poraba_prteden[is.na(test$poraba_prteden)] <- 0

kn5_MSE <- kknn(poraba ~ poraba_prteden + povrsina, train, test, k = 5)
kn10_MSE <- kknn(poraba ~ poraba_prteden + povrsina, train, test, k = 10)
kn20_MSE <- kknn(poraba ~ poraba_prteden + povrsina, train, test, k = 20)

kn5_rF <- kknn(poraba ~ poraba_prteden + povrsina + leto_izgradnje, train, test, k = 5)
kn10_rF <- kknn(poraba ~ poraba_prteden + povrsina + leto_izgradnje, train, test, k = 10)
kn20_rF <- kknn(poraba ~ poraba_prteden + povrsina + leto_izgradnje, train, test, k = 20)

kn5_RFM <- kknn(poraba ~ poraba_prteden + povrsina + leto_izgradnje, train, test, k = 5)
kn10_RFM <- kknn(poraba ~ poraba_prteden + povrsina + leto_izgradnje, train, test, k = 10)
kn20_RFM <- kknn(poraba ~ poraba_prteden + povrsina + leto_izgradnje, train, test, k = 20)


train$poraba_prteden[train$poraba_prteden == 0] <- NA
test$poraba_prteden[test$poraba_prteden == 0] <- NA

# Linear regression - cross check, skip one

testTrans <- test
testTrans$poraba <- log1p(testTrans$poraba)
testTrans$poraba_prteden <- log1p(testTrans$poraba_prteden)

logPredicted <- vector()

for (i in 1:nrow(testTrans))
{	
  model <- lm(poraba ~ ., testTrans[-i,])
  logPredicted[i] <- predict(model, testTrans[i,])
}

predicted_cs <- expm1(logPredicted)



## Regression models - evaluations

# Get mean value of target attribute
mv <- mean(train$poraba)

# Get observed attribute
observed_r <- test$poraba

# Predict 
predicted_r <- predict(lr_all_attributes, test)

# Evaluate model
lra <- format(c(mae(observed_r, predicted_r), 
         rmae(observed_r, predicted_r, mv), 
         mse(observed_r, predicted_r), 
         rmse(observed_r, predicted_r, mv)), scientific = FALSE)

# Name columns in model
names(lra) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- predict(lr_MSE, test)

lrMSE <- format(c(mae(observed_r, predicted_r), 
           rmae(observed_r, predicted_r, mv), 
           mse(observed_r, predicted_r), 
           rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(lrMSE) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- predict(lr_rF, test)

lrRF <- format(c(mae(observed_r, predicted_r), 
                 rmae(observed_r, predicted_r, mv), 
                 mse(observed_r, predicted_r), 
                 rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(lrRF) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- predict(lr_RFM, test)

lrRFM <- format(c(mae(observed_r, predicted_r), 
                  rmae(observed_r, predicted_r, mv), 
                  mse(observed_r, predicted_r), 
                  rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(lrRFM) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- predict(rt_all_attributes, test)

rta <- format(c(mae(observed_r, predicted_r), 
                rmae(observed_r, predicted_r, mv), 
                mse(observed_r, predicted_r), 
                rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(rta) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- predict(rt_MSE, test)

rtMSE <- format(c(mae(observed_r, predicted_r), 
                  rmae(observed_r, predicted_r, mv), 
                  mse(observed_r, predicted_r), 
                  rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(rtMSE) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- predict(rt_rF, test)

rtRF <- format(c(mae(observed_r, predicted_r), 
                 rmae(observed_r, predicted_r, mv), 
                 mse(observed_r, predicted_r), 
                 rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(rtRF) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- predict(rt_RFM, test)

rtRFM <- format(c(mae(observed_r, predicted_r), 
                  rmae(observed_r, predicted_r, mv), 
                  mse(observed_r, predicted_r), 
                  rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(rtRFM) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn5_all_attributes)

kn5a <- format(c(mae(observed_r, predicted_r), 
                 rmae(observed_r, predicted_r, mv), 
                 mse(observed_r, predicted_r), 
                 rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn5a) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn10_all_attributes)

kn10a <- format(c(mae(observed_r, predicted_r), 
                  rmae(observed_r, predicted_r, mv), 
                  mse(observed_r, predicted_r), 
                  rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn10a) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn20_all_attributes)

kn20a <- format(c(mae(observed_r, predicted_r), 
                  rmae(observed_r, predicted_r, mv), 
                  mse(observed_r, predicted_r), 
                  rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn20a) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn5_MSE)

kn5MSE <- format(c(mae(observed_r, predicted_r), 
                   rmae(observed_r, predicted_r, mv), 
                   mse(observed_r, predicted_r), 
                   rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn5MSE) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn10_MSE)

kn10MSE <- format(c(mae(observed_r, predicted_r), 
                    rmae(observed_r, predicted_r, mv), 
                    mse(observed_r, predicted_r), 
                    rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn10MSE) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn20_MSE)

kn20MSE <- format(c(mae(observed_r, predicted_r), 
                    rmae(observed_r, predicted_r, mv), 
                    mse(observed_r, predicted_r), 
                    rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn20MSE) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn5_rF)

kn5rF <- format(c(mae(observed_r, predicted_r), 
                  rmae(observed_r, predicted_r, mv), 
                  mse(observed_r, predicted_r), 
                  rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn5rF) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn10_rF)

kn10rF <- format(c(mae(observed_r, predicted_r), 
                   rmae(observed_r, predicted_r, mv), 
                   mse(observed_r, predicted_r), 
                   rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn10rF) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn20_rF)

kn20rF <- format(c(mae(observed_r, predicted_r), 
                   rmae(observed_r, predicted_r, mv), 
                   mse(observed_r, predicted_r), 
                   rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn20rF) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- predict(kn5_RFM)

kn5RFM <- format(c(mae(observed_r, predicted_r), 
                   rmae(observed_r, predicted_r, mv), 
                   mse(observed_r, predicted_r), 
                   rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn5RFM) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn10_RFM)

kn10RFM <- format(c(mae(observed_r, predicted_r), 
                    rmae(observed_r, predicted_r, mv), 
                    mse(observed_r, predicted_r), 
                    rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn10RFM) <- c("mae", "rmae", "mse", "rmse")


predicted_r <- fitted(kn20_RFM)

kn20RFM <- format(c(mae(observed_r, predicted_r), 
                    rmae(observed_r, predicted_r, mv), 
                    mse(observed_r, predicted_r), 
                    rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(kn20RFM) <- c("mae", "rmae", "mse", "rmse")


lrCS <- format(c(mae(observed_r, predicted_r), 
                 rmae(observed_r, predicted_r, mv), 
                 mse(observed_r, predicted_r), 
                 rmse(observed_r, predicted_r, mv)), scientific = FALSE)

names(lrCS) <- c("mae", "rmae", "mse", "rmse")


# Create dataframe from values

# Create empty dataframe
rm(Eval_df)

Eval_df <- data.frame(mae = character(), rmae = character(), mse = character(), rmse = character())

Eval_df <- rbind(t(lra), Eval_df)
Eval_df <- rbind(t(lrMSE), Eval_df)
Eval_df <- rbind(t(lrRF), Eval_df)
Eval_df <- rbind(t(lrRFM), Eval_df)
Eval_df <- rbind(t(lrCS), Eval_df)

Eval_df <- rbind(t(rta), Eval_df)
Eval_df <- rbind(t(rtMSE), Eval_df)
Eval_df <- rbind(t(rtRF), Eval_df)
Eval_df <- rbind(t(rtRFM), Eval_df)

Eval_df <- rbind(t(kn5a), Eval_df)
Eval_df <- rbind(t(kn5MSE), Eval_df)
Eval_df <- rbind(t(kn5rF), Eval_df)
Eval_df <- rbind(t(kn5RFM), Eval_df)

Eval_df <- rbind(t(kn10a), Eval_df)
Eval_df <- rbind(t(kn10MSE), Eval_df)
Eval_df <- rbind(t(kn10rF), Eval_df)
Eval_df <- rbind(t(kn10RFM), Eval_df)

Eval_df <- rbind(t(kn20a), Eval_df)
Eval_df <- rbind(t(kn20MSE), Eval_df)
Eval_df <- rbind(t(kn20rF), Eval_df)
Eval_df <- rbind(t(kn20RFM), Eval_df)

row.names(Eval_df) <- c("lra", "lrMSE", "lrRF", "lrRFM", "lrCS",
                        "rta", "rtMSE", "rtRF", "rtRFM",
                        "k5a", "k5MSE", "k5RF", "k5RFM",
                        "k10a", "k10MSE", "k10RF", "k10RFM",
                        "k20a", "k20MSE", "k20RF", "k20RFM")


Eval_df <- Eval_df[order(Eval_df$rmae), , drop = FALSE]






### Comparison of models trained in one region vs both regions

## Test models using only all attributes (no subgroups)
## Test decision tree for classification and linear regression for regression

dt_east <- rpart(namembnost ~ ., data = train[train$regija == "vzhodna", ])
dt_west <- rpart(namembnost ~ ., data = train[train$regija == "zahodna", ])

obs_limited <- test$namembnost[test$regija == "vzhodna"]

predicted <- predict(dt_east, test[test$regija == "vzhodna", ], type = "class")
dtEast_CA <- CA(obs_limited, predicted)

obs_limited <- test$namembnost[test$regija == "zahodna"]

predicted <- predict(dt_west, test[test$regija == "zahodna", ], type = "class")
dtWest_CA <- CA(obs_limited, predicted)



lr_east <- lm(poraba ~ ., data = train[, c(-1)], subset = (train$regija == "vzhodna"))
lr_west <- lm(poraba ~ ., data = train[, c(-1)], subset = (train$regija == "zahodna"))

obs_limited_r <- test$poraba[test$regija == "vzhodna" & test$namembnost != "stanovanjska"]

predicted_r <- predict(lr_east, test[test$regija == "vzhodna" & test$namembnost != "stanovanjska", ])

# Evaluate model
lre <- format(c(mae(obs_limited_r, predicted_r), 
                rmae(obs_limited_r, predicted_r, mv), 
                mse(obs_limited_r, predicted_r), 
                rmse(obs_limited_r, predicted_r, mv)), scientific = FALSE)

# Name columns in model
names(lre) <- c("mae", "rmae", "mse", "rmse")

obs_limited_r <- test$poraba[test$regija == "zahodna"]

predicted_r <- predict(lr_west, test[test$regija == "zahodna", ])

# Evaluate model
lrw <- format(c(mae(obs_limited_r, predicted_r), 
                rmae(obs_limited_r, predicted_r, mv), 
                mse(obs_limited_r, predicted_r), 
                rmse(obs_limited_r, predicted_r, mv)), scientific = FALSE)

# Name columns in model
names(lrw) <- c("mae", "rmae", "mse", "rmse")






### Combine methods of machine learning

## Voting

predDT <- predict(dt_all_attributes, test, type = "class")
predNB <- predict(nb_all_attributes, test, type = "class")
predKNN <- predict(knn_all_attributes5, test, type = "class")

pred_df_voting <- data.frame(predDT, predNB, predKNN)

predNamembnost <- voting(pred_df_voting)

predicted <- factor(predNamembnost, levels = levels(train$namembnost))
voting_CA <- CA(test$namembnost, predicted)

## Weighted voting

predDTW <- predict(dt_all_attributes, test, type = "prob")
predNBW <- predict(nb_all_attributes, test, type = "prob")
predKNNW <- predict(knn_all_attributes5, test, type = "prob")

pred_df_Wvoting <- predDTW + predNBW + predKNNW

predNamembnost <- colnames(pred_df_Wvoting)[max.col(pred_df_Wvoting)]
predicted <- factor(predNamembnost, levels = levels(train$namembnost))

Wvoting_CA <- CA(test$namembnost, predicted)

## Bagging

n <- nrow(train)
m <- 30

models <- list()
for (i in 1:m)
{
  sel <- sample(1:n, n, replace = T)
  bootstrap <- train[sel, ]
  models[[i]] <- CoreModel(namembnost ~ ., bootstrap, model = "tree", minNodeWeightTree = 1)
}

pred_df_bagging <- NULL
for (i in 1:m)
{
  pred_df_bagging <- cbind(pred_df_bagging, as.character(predict(models[[i]], test, type = "class")))
}
predNamembnost <- voting(pred_df_bagging)
predicted <- factor(predNamembnost, levels = levels(train$namembnost))
bagging_CA <- CA(test$namembnost, predicted)








### Cronology of data

# Combine dataframes and order by building and date 
df_combined <- rbind(train, test)
#df_combined <- df_combined

# Get smaller dataframes which only contain data for 1 month
sets <- split(df_combined, f = df_combined$mesec)

CA_set <- list()
REG_set <- list()

# Loop through all months but last
for (i in 1:(length(sets) - 1))
{
  # Create empty dataframe with same columns
  train_set <- df_combined[0, ]
  
  # Loop through all months until current month (inclusive)
  for (j in 1:i) 
  {
    # Add data of next month to dataframe
    train_set <- rbind(train_set, sets[[j]])
  }
  
  
  ## Classification models
  
  # Create classification models that are trained on data from all months until now
  dt_months <- rpart(namembnost ~ ., data = train_set)
  nb_months <- CoreModel(namembnost ~ ., data = train_set, model = "bayes")
  kn5_months <- CoreModel(namembnost ~ ., data = train_set, model = "knn", kInNN = 5)
  
  # Predict data for next month using classification models
  predDTm <- predict(dt_months, sets[[i + 1]], type = "class")
  predNBm <- predict(nb_months, sets[[i + 1]], type = "class")
  predKNNm <- predict(kn5_months, sets[[i + 1]], type = "class")
  
  # Get observed values
  observed_months <- sets[[i + 1]]$namembnost
  
  # Calculate classification accuracy of models
  vec <- c(CA(observed_months, predDTm), 
           CA(observed_months, predNBm),
           CA(observed_months, predKNNm))
  
  # Store calculations to list
  CA_set[[i]] <- vec
  
  
  ## Regression models
  lr_months <- lm(poraba ~ ., data = train_set[, c(-13, -14)])
  rt_months <- rpart(poraba ~ ., data = train_set[, c(-13, -14)], cp = 0)
  
  tab <-printcp(rt_months)
  
  # Get cp that is equal to minimal error of internal cross check
  row <- which.min(tab[, "xerror"])
  th <- mean(c(tab[row, "CP"], tab[row - 1, "CP"]))
  
  # Prune tree with new cp value
  rt_months <- prune(rt_months, cp = th)
  
  predLrm <- predict(lr_months, as.data.frame(sets[[i + 1]])[, c(-13, -14)])
  predRtm <- predict(rt_months, as.data.frame(sets[[i + 1]])[, c(-13, -14)])
  
  observed_months_r <- sets[[i + 1]]$poraba
  
  mv <- mean(sets[[i + 1]]$poraba)
  
  lrm <- format(c(mae(observed_months_r, predLrm), 
                  rmae(observed_months_r, predLrm, mv), 
                  mse(observed_months_r, predLrm), 
                  rmse(observed_months_r, predLrm, mv)), scientific = FALSE)
  
  # Name columns in model
  names(lrm) <- c("mae", "rmae", "mse", "rmse")
  
  rtm <- format(c(mae(observed_months_r, predRtm), 
                  rmae(observed_months_r, predRtm, mv), 
                  mse(observed_months_r, predRtm), 
                  rmse(observed_months_r, predRtm, mv)), scientific = FALSE)
  
  # Name columns in model
  names(rtm) <- c("mae", "rmae", "mse", "rmse")
  
  # Create empty list 
  vv <- list()
  
  # Set list values to calculations
  vv[[1]] <- lrm
  vv[[2]] <- rtm
 
  # Store list in set of all values
  REG_set[[i]] <- vv
}



