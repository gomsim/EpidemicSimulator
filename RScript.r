args = commandArgs(trailingOnly=TRUE)
setwd(args[3])
data <- read.csv(file = args[1], header = TRUE, sep=",",colClasses = NA, stringsAsFactor = FALSE, na.strings = "?")
dir.create("plots")
path <- args[2]

# Libraries
library(ggplot2)
library(dplyr)
library(gridExtra)
library(poweRlaw)

#PDF
pdf(file=paste(path, tools::file_path_sans_ext(basename(args[1])), ".pdf", sep=""))

#Plot size/tick graph   tail(3869) %>%
data %>%
  tail(3869) %>%
  ggplot( aes(x=tick, y=size)) +
  geom_line(color="darkgrey", alpha =0.7) +
  geom_point(color="black",alpha=0.7) + xlab("Tick") + ylab("Storlek")

#Aggregate data to size/freq(size)
aggregatedSum <- data
colnames(aggregatedSum) <- c("size", "sum")
aggregatedSum$sum <- 1
aggregatedSum <- aggregatedSum %>% 
  group_by(size) %>% 
  summarise_all(funs(sum))

#Plot histogram
plot(aggregatedSum, pch = 20, main = "", xlab = "Storlek", ylab = "Frekvens")

#Remove duplicates
unique <- aggregatedSum[!duplicated(aggregatedSum$sum), ]

#Plot size/freq(size) on log-log graph
plot(aggregatedSum, pch = 20, main="", xlab ="log(storlek)", ylab= "log(Frekvens(storlek))", log="yx")
#Least-squared linear regression on both unique and data with duplicates
abline(lm(log10(unique$sum)~log10(unique$size)), col ="red" )
abline(lm(log10(aggregatedSum$sum)~log10(aggregatedSum$size)), col ="blue" )
#Plot unique size/freq(size) on log-log graph
plot(unique, pch = 20, main="", xlab = "log(storlek)", ylab="log(Frekvens(storlek))", log="yx")
#Least-squared linear regression on data
abline(lm(log10(unique$sum)~log10(unique$size)), col ="red" )
uregression <- lm(log10(unique$sum)~log10(unique$size))
nregression <- lm(log10(aggregatedSum$sum)~log10(aggregatedSum$size))
plot.new()
cof <- matrix(uregression$coefficients)
cof <- cbind(cof, nregression$coefficients)
rownames(cof) <- c("Intercept", "alpha")
colnames(cof) <- c("unique", "normal")
grid.table(cof)

data_pl <- displ$new(aggregatedSum$sum)
est_xmin <- estimate_xmin(data_pl)
data_pl$setXmin(est_xmin)

#Plot CDF/Freq(size)
plot(data_pl, xlab= "Frekvens", ylab="CDF")
lines(data_pl, col = "red")

bs_p = bootstrap_p(data_pl, threads = 12)

#plot(bs_p)

data_ln = dislnorm$new(aggregatedSum$sum)

est = estimate_xmin(data_ln)
data_ln$setXmin(est)
data_ln$setXmin(data_pl$getXmin())
est = estimate_pars(data_ln)
data_ln$setPars(est)
lines(data_ln, col = "green")

data_exp <- disexp$new(aggregatedSum$sum)
est <- estimate_xmin(data_exp)
data_exp$setXmin(est)
data_exp$setXmin(data_pl$getXmin())
est <- estimate_pars(data_exp)
data_exp$setPars(est)
lines(data_exp, col="blue")

cof <- matrix(ncol = 3)
cof <- rbind(c(bs_p$p,data_pl$pars, data_pl$xmin ))
colnames(cof) <- c("p", "alpha","xmin")
plot.new()
grid.table(cof)

compare_ln <- compare_distributions(data_pl, data_ln)
compare_exp <- compare_distributions(data_pl, data_exp)

likelihoodratio <- matrix(nrow= 2, ncol = 2)
likelihoodratio <- rbind(c(compare_ln$test_statistic, compare_ln$p_two_sided))
likelihoodratio <- rbind(likelihoodratio, c(compare_exp$test_statistic, compare_exp$p_two_sided))
colnames(likelihoodratio) <- c("ks", "p")
rownames(likelihoodratio) <- c("Log-Normal", "Exponential")
plot.new()
grid.table(likelihoodratio)

dev.off()
print("Rscript done!")