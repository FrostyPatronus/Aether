package main;

/**
 * Created by Timothy on 1/11/2016.
 */
public class Model {

    private double bookLength, finSpan, ppd, tpp, ded;
    private boolean isAccurate;

    public void setAccurate(boolean accurate) {
        isAccurate = accurate;
    }

    public String setFinspan(double bookLength, double ppd) {
        finSpan = (isAccurate)? bookLength / ppd : Math.ceil(bookLength / ppd);
        return (isAccurate)? String.format("%.2f", getFinSpan()) : String.valueOf((int)getFinSpan());
    }

    public void setFinSpan(double finSpan){
        this.finSpan = finSpan;
    }

    public String setPpd(double bkLength, double timeSpan) {
        ppd = (isAccurate)? bkLength / timeSpan : Math.ceil(bkLength / timeSpan);
        return (isAccurate)? String.format("%.2f", getPpd()) : String.valueOf((int)getPpd());
    }

    public String setTpp(double tded, double ppd) {
        tpp =  Math.ceil((60 * tded) / ppd);
        return String.valueOf((int)getTpp());
    }

    public String setDed(double ppd, double tpp) {
        ded = Math.ceil((ppd * tpp) / 60);
        return String.valueOf((int)getDed());
    }

    public double getFinSpan() {
        if (finSpan < 0) return 0;
        else return finSpan;
    }

    public double getPpd() {
        if (ppd < 0) return 0;
        return ppd;
    }

    public double getTpp() {
        if (tpp < 0) return 0;
        return tpp;
    }

    public double getDed() {
        if (ded < 0) return 0;
        return ded;
    }

    public void setBookLength(double bookLength) {
        this.bookLength = bookLength;
    }

    public double getBookLength() {
        return bookLength;
    }

    public boolean isAccurate() {
        return isAccurate;
    }
}
