var c=5;
var t;
var timer_is_on=0;

function timedCount()
{
	if (c==0) {
		alert("Tiden er ute!");
		location="index.xhtml"
	}
document.getElementById('timer:txt').value=c;
c=c-1;
t=setTimeout("timedCount()",1000);
}

function doTimer()
{
if (!timer_is_on)
  {
  timer_is_on=1;
  timedCount();
  }
}