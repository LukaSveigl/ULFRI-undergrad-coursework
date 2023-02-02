# Function to calculate classification accuracy
CA <- function(obs, pred)
{
  tab <- table(obs, pred)
  
  return(sum(diag(tab)) / sum(tab))
}

# Function to calculate mean absolute error
mae <- function(obs, pred)
{
  return(mean(abs(obs - pred), na.rm=TRUE))
}

# Function to calculate mean square error
mse <- function(obs, pred)
{
  return(mean((obs - pred)^2, na.rm=TRUE))
}

# Function to calculate relative mean absolute error
rmae <- function(obs, pred, mean.val) 
{  
  return(sum(abs(obs - pred), na.rm=TRUE) / sum(abs(obs - mean.val), na.rm=TRUE))
}

# Function to calculate relative mean square error
rmse <- function(obs, pred, mean.val) 
{  
  return(sum((obs - pred)^2, na.rm=TRUE)/sum((obs - mean.val)^2, na.rm=TRUE))
}

# Function to calculate class with most votes
voting <- function(predictions)
{
  res <- vector()
  
  for (i in 1 : nrow(predictions))  	
  {
    vec <- unlist(predictions[i,])
    res[i] <- names(which.max(table(vec)))
  }
  
  res
}
