package br.com.project.report.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class ReportUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String UNDERLINE = "_";
    private static final String FOLDER_RELATORIOS = "/relatorios";
    private static final String SUBREPORT_DIR = "SUBREPORT_DIR";
    private static final String EXTESION_ODS = "ods";
    private static final String EXTESION_XLS = "xls";
    private static final String EXTESION_HTML = "html";
    private static final String EXTESION_PDF = "pdf";
    private String SEPARATOR = File.separator;
    private static final int RELATORIO_PDF = 1;
    private static final int RELATORIO_XLS = 2;
    private static final int RELATORIO_HTML = 3;
    private static final int RELATORIO_PLANILHA_OPEN_OFFICE = 4;
    private static final String POINT = ".";
    private StreamedContent returnArchive = null;
    private String reportWay = null;
    private JRExporter exportedTypeArchive = null;
    private String extensionExportedArchive = "";
    private String subReportWay_dir = "";
    private File generateArchive = null;

    public StreamedContent generateReport(List<?> listDataBeanCollectionReport, HashMap parameterReport, String nameReportJasper, String nameReportExit, int reportType) throws Exception {
        //Cria a lista de collectionDataSource de beans que carregam os dados para o relatorio
        JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDataBeanCollectionReport);

        //Fornece o caminho fisico ate a pasta que contem os relatorios compilados .jasper
        FacesContext context = FacesContext.getCurrentInstance();
        context.responseComplete();
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

        String caminhoRelatorio = servletContext.getRealPath(FOLDER_RELATORIOS);

        File file = new File(caminhoRelatorio + SEPARATOR + nameReportJasper + POINT + "jasper");

        if(Objects.nonNull(caminhoRelatorio) || (Objects.isNull(caminhoRelatorio) && caminhoRelatorio.isEmpty()) || !file.exists()){
            caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();
            SEPARATOR = "";
        }

        //Caminho para imagens
        parameterReport.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);

        //Caminho completo ate o relatorio compilado indicado
        String caminhoArquivoJasper = caminhoRelatorio + SEPARATOR + nameReportJasper + POINT + "jasper";

        //Faz o carregamento do relatorio indicado
        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(caminhoRelatorio);

        //Seta o parametro SUBREPORT_DIR como caminho físico para sub-reports
        subReportWay_dir = caminhoRelatorio + SEPARATOR;
        parameterReport.put(SUBREPORT_DIR, subReportWay_dir);

        //Carrega o arquivo compilado para a memória
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterReport, jrbcds);

        switch (reportType){
            case RELATORIO_PDF:
                exportedTypeArchive = new JRPdfExporter();
                extensionExportedArchive = EXTESION_PDF;
                break;
            case RELATORIO_HTML:
                exportedTypeArchive = new JRHtmlExporter();
                extensionExportedArchive = EXTESION_HTML;
                break;
            case RELATORIO_XLS:
                exportedTypeArchive = new JRXlsExporter();
                extensionExportedArchive = EXTESION_XLS;
                break;
            case RELATORIO_PLANILHA_OPEN_OFFICE:
                exportedTypeArchive = new JROdtExporter();
                extensionExportedArchive = EXTESION_ODS;
                break;
            default:
                exportedTypeArchive = new JRPdfExporter();
                extensionExportedArchive = EXTESION_PDF;
                break;
        }
        nameReportExit += UNDERLINE + DateUtils.getDateAtualReportName();

        //Caminho relatorio exportado
        reportWay = caminhoRelatorio + SEPARATOR + nameReportExit + POINT + extensionExportedArchive;

        //Cria novo arquivo exportado
        generateArchive = new File(reportWay);

        //Preparar impressao
        exportedTypeArchive.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

        //Nome do arquivo fisico a ser exportado
        exportedTypeArchive.setParameter(JRExporterParameter.OUTPUT_FILE, generateArchive);

        //Executa a exportacao
        exportedTypeArchive.exportReport();

        //Remove o arquivo do servidor após o download do usuario
        generateArchive.deleteOnExit();

        //Cria o inputstream para ser usado pelo PrimeFaces
        InputStream conteudoRelatorio = new FileInputStream(generateArchive);

        //Faz o retorno para a aplicacao
        returnArchive = new DefaultStreamedContent(conteudoRelatorio, "application/" + extensionExportedArchive, nameReportExit + POINT + extensionExportedArchive);

        return returnArchive;
    }

}
