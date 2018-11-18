package br.com.project.report.util;

import br.com.project.util.all.BeanViewAbstract;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public abstract class BeanReportView extends BeanViewAbstract implements Serializable {

    private static final long serialVersionUID = 1L;

    protected StreamedContent archiveReport;
    protected int typeReport;
    protected List<?> listDataBeanCollectionReport;
    protected HashMap<Object, Object> parameterReport;
    protected String nameReportJasper = "default";
    protected String nameReportExit = "default";

    @Autowired
    private ReportUtil reportUtil;

    public BeanReportView(){
        parameterReport = new HashMap<>();
        listDataBeanCollectionReport = new ArrayList<>();
    }

    public ReportUtil getReportUtil(){
        return reportUtil;
    }

    public void setReportUtil(ReportUtil reportUtil){
        this.reportUtil = reportUtil;
    }

    public StreamedContent getArchiveReport() throws Exception{
        return getReportUtil().generateReport(getListDataBeanCollectionReport(),
                getParameterReport(), getNameReportJasper(), getNameReportExit(), getTypeReport());
    }

    public int getTypeReport(){
        return typeReport;
    }

    public void setTypeReport(int typeReport){
        this.typeReport = typeReport;
    }

    public List<?> getListDataBeanCollectionReport(){
        return listDataBeanCollectionReport;
    }

    public void setListDataBeanCollectionReport(List<?> listDataBeanCollectionReport) {
        this.listDataBeanCollectionReport = listDataBeanCollectionReport;
    }

    public HashMap<Object, Object> getParameterReport(){
        return parameterReport;
    }

    public void setParameterReport(HashMap<Object, Object> parameterReport) {
        this.parameterReport = parameterReport;
    }

    public String getNameReportJasper(){
        return nameReportJasper;
    }

    public void setNameReportJasper(String nameReportJasper) {
        if(Objects.nonNull(nameReportJasper) || nameReportJasper.isEmpty()){
            nameReportJasper = "default";
        }
        this.nameReportJasper = nameReportJasper;
    }

    public String getNameReportExit(){
        return nameReportExit;
    }

    public void setNameReportExit(String nameReportExit) {
        if(Objects.nonNull(nameReportExit) || nameReportExit.isEmpty()){
            nameReportExit = "default";
        }
        this.nameReportExit = nameReportExit;
    }
}