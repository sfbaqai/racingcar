	speed <<- c(0)
radius <<- c()
plot_data <- function(name="temp3.csv",header=FALSE,sp=1,rd=2){
	speed<<-c()
	radius<<-c()
	m <- read.csv(name,header=header)
	i <- (-m[,sp]<360)
	speed<<-c(speed,-m[,sp][i])
	radius<<-c(radius,m[,rd][i])
	#g <- lm(radius ~ speed+I(speed^2)+I(speed^3)+I(speed^4)+I(speed^5))
	#g <- lm(radius ~ speed+I(speed^2)+I(speed^3)+I(speed^4))
	#g <- lm(radius ~ speed+I(speed^2)+I(speed^3))
	#g <- lm(radius ~ speed+I(speed^2)+I(speed^3)+I(speed^4)+I(speed^5)+I(1/speed)+I(1/speed^2)+I(1/speed^3))
	#g <- lm(radius ~ speed+I(speed^2)+I(speed^3)+I(exp(1/speed)))
	#g <- lm(radius ~ I(1/speed)+I(1/speed^2)+I(1/speed^3)+I(1/speed^4))
	g <- lm(radius ~ speed+I(speed^2)+I(speed^3)+I(speed^4)+I(speed^5)+I(1/speed)+I(1/speed^2)+I(1/speed^3)+I(1/speed^4)+I(1/speed^5)+I(log(speed)))
	#g <- lm(radius ~ speed+I(speed^2)+I(speed^3)+I(speed^4)+I(speed^5)+I(1/speed)+I(1/speed^2)+I(1/speed^3)+I(1/speed^4)+I(1/speed^5))
	#g <- lm(radius ~ I(exp(1/speed))+I(exp(1/speed/speed))+I(exp(1/speed/speed/speed))+I(exp(1/speed/speed/speed/speed)) )
	plot(radius~speed)
	lines(g$fitted.values~speed)
	summary(g)
	#print(g)
	#print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*x+",g$coefficients[[3]],"*x*x+",g$coefficients[[4]],"*x*x*x",sep=""))
	#print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*x+",g$coefficients[[3]],"*x2+",g$coefficients[[4]],"*x3+",g$coefficients[[5]],"*x4",sep=""))
	#print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*x+",g$coefficients[[3]],"*x2+",g$coefficients[[4]],"*x3+",g$coefficients[[5]],"*x4+",g$coefficients[[6]],"*x5",sep=""))
	#print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*x+",g$coefficients[[3]],"*x*x+",g$coefficients[[4]],"*x*x*x+",g$coefficients[[5]],"*exp(1/x)",sep=""))
	#print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*x+",g$coefficients[[3]],"*x*x+",g$coefficients[[4]],"*x*x*x+",g$coefficients[[5]],"*log(x)",sep=""))

	#print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*x+",g$coefficients[[3]],"*x2+",g$coefficients[[4]],"*x3+",g$coefficients[[5]],"*x4+"
	#	,g$coefficients[[6]],"*x5+",g$coefficients[[7]],"/x+",g$coefficients[[8]],"/x2+",g$coefficients[[9]],"/x3",sep=""))

	#print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*x+",g$coefficients[[3]],"*x2+",g$coefficients[[4]],"*x3+",g$coefficients[[5]],"*x4+"
	#	,g$coefficients[[6]],"*x5+",g$coefficients[[7]],"/x+",g$coefficients[[8]],"/x2+",g$coefficients[[9]],"/x3+",g$coefficients[[10]],"/x4+",g$coefficients[[11]],"/x5",sep=""))


	#print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*x+",g$coefficients[[3]],"*x2+",g$coefficients[[4]],"*x3+",g$coefficients[[5]],"*x4+"
	#	,g$coefficients[[6]],"*x5+",g$coefficients[[7]],"/x+",g$coefficients[[8]],"/x2+",g$coefficients[[9]],"/x3",sep=""))

	print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*x+",g$coefficients[[3]],"*x2+",g$coefficients[[4]],"*x3+",g$coefficients[[5]],"*x4+"
		,g$coefficients[[6]],"*x5+",g$coefficients[[7]],"/x+",g$coefficients[[8]],"/x2+",g$coefficients[[9]],"/x3+",g$coefficients[[10]],"/x4+",g$coefficients[[11]],"/x5+",g$coefficients[[12]],"*Math.log(x)",sep=""))


	#f <<- function(x){
	#	g$coefficients[[1]]+g$coefficients[[2]]*x+g$coefficients[[3]]*x*x
	#}
	#g <- rlm(speed ~ radius+I(radius^2))
	#plot(speed~radius)
	#lines(g$fitted.values~radius)
}

process <- function(name="C:\\WCCI\\speed-radius.csv",header=TRUE){	
 	m <- read.csv(name,header=header)
	for (x in m[,1]){
		i <- which(x==m[1])
		s <- max(m[i,7])
		n <- which(m[i,7]==s)[1]
		r <- m[i[n],8]
		#if (s*3.6>291.6){
		speed <<- c(speed,s*3.6)
		radius <<- c(radius,r)
		#}
	};
	g <<- lm(radius ~ speed+I(speed^2))
	plot(radius~speed)
	lines(g$fitted.values~speed)	

	#g <<- lm(speed ~ radius+I(radius^2)+I(radius^3))
	#plot(speed~radius)
	#lines(g$fitted.values~radius)	
	print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*speedkmh+",g$coefficients[[3]],"*speedkmh*speedkmh",sep=""))
	summary(g)
	f <<- function(x){
		g$coefficients[[1]]+g$coefficients[[2]]*x+g$coefficients[[3]]*x*x
	}

}

circle <- function(name="c:\\wcci\\temp.txt"){
	m<-read.table(name)
	speed<<-c(speed,m[,1][m[,2]>180])
	radius<<-c(radius,m[,2][m[,2]>180])
	g <- glm(radius ~ speed+I(speed^2))
	plot(radius~speed)
	lines(g$fitted.values~speed)
	#g <- rlm(speed ~ radius+I(radius^2))
	#plot(speed~radius)
	#lines(g$fitted.values~radius)
}