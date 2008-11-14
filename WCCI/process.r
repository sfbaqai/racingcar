speed <<- c(0)
radius <<- c()
plot_data <- function(name="temp3.csv",header=FALSE,sp=1,rd=2){
	speed<<-c()
	radius<<-c()
	m <- read.csv(name,header=header)
	i <- m[,sp]<450
	speed<<-c(speed,m[,sp][i])
	radius<<-c(radius,m[,rd][i])
	g <- lm(radius ~ speed+I(speed^2)+I(speed^3))
	plot(radius~speed)
	lines(g$fitted.values~speed)
	summary(g)

	print(paste(g$coefficients[[1]],"+",g$coefficients[[2]],"*x+",g$coefficients[[3]],"*x*x+",g$coefficients[[4]],"*x*x*x",sep=""))
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