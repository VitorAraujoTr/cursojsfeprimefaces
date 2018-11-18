package br.com.project.report.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String getDateAtualReportName(){
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyy");
        return dateFormat.format(Calendar.getInstance().getTime());
    }



}
