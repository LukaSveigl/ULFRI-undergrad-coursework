plottdf.ltail<- function(df=44,alpha=0.05,ind=FALSE,t=-3)
{
  x<-seq(-4,4,by=0.0001)
  y<-dt(x,df)
  c<-round(qt(alpha,df),3)
  par(mar=c(3,2,3,1))
  plot(0,xlim=c(-4,4),ylim=c(0,0.4),ylab="",axes=F,type="n", xlab="",
       main=bquote(paste("Studentova ", t[.(df)], " porazdelitev",sep=" ")))
  axis(1,pos=0,at=seq(-4,4,by=0.1),tcl=0,labels=F)
  axis(2,pos=0,at=seq(0,0.4,by=0.01),tcl=0,labels=F)
  lines(x,y,lwd=2)
  polygon(x=c(-4,x[x>=-4&x<=c],c), y=c(0,y[x>=-4&x<=c],0), col="grey70")
  if(ind){
   axis(1,pos=0,at=c)
   polygon(x=c(-4,x[x>=-4&x<=t],t), y=c(0,y[x>=-4&x<=t],0), col="blue")
   axis(1,pos=0,at=t)
  }else{
   axis(1,pos=0,at=c,labels="c")
  }
}


plottdf.rtail<- function(df=44,alpha=0.05,ind=FALSE,t=3)
{
  x<-seq(-4,4,by=0.0001)
  y<-dt(x,df)
  c<-round(qt(1-alpha,df),3)
  par(mar=c(3,2,3,1))
  plot(0,xlim=c(-4,4),ylim=c(0,0.4),ylab="",axes=F,type="n", xlab="",
       main=bquote(paste("Studentova ", t[.(df)], " porazdelitev",sep=" ")))
  axis(1,pos=0,at=seq(-4,4,by=0.1),tcl=0,labels=F)
  axis(2,pos=0,at=seq(0,0.4,by=0.01),tcl=0,labels=F)
  lines(x,y,lwd=2)
  polygon(x=c(x[x>=c],4,c), y=c(y[x>=c],0,0), col="grey70")
  if(ind){
   axis(1,pos=0,at=c)
   polygon(x=c(x[x>=t],4,t), y=c(y[x>=t],0,0), col="blue")
   axis(1,pos=0,at=t)
  }else{
   axis(1,pos=0,at=c,labels="c")
  }
}


 