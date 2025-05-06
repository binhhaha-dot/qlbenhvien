package qlbv;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import model.Report;
import serveice.ReportDAO;

public class ReportForm extends JPanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private ReportDAO reportDAO;

    public ReportForm() {
        setLayout(new BorderLayout());
        reportDAO = new ReportDAO();
        // Tiêu đề
        JLabel title = new JLabel("Báo cáo thống kê", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Bảng hiển thị dữ liệu thống kê
        String[] columns = {"Mô tả", "Số liệu"};
        tableModel = new DefaultTableModel(columns, 0);
        reportTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(reportTable);
        add(tableScrollPane, BorderLayout.CENTER);
        JPanel panelButton = new JPanel(new FlowLayout());
        JButton btnShowChart = new JButton("Hiển thị biểu đồ");
        btnShowChart.addActionListener(e -> showReportChart());
        JButton btnShowBarChart = new JButton("Hiển thị biểu đồ cột");
        btnShowBarChart.addActionListener(e -> showBarChart());

        // Nút tải dữ liệu
        JButton btnLoadData = new JButton("Tải dữ liệu");
        btnLoadData.addActionListener(e -> loadReportData());
        panelButton.add(btnShowBarChart);
        panelButton.add(btnShowChart);
        panelButton.add(btnLoadData);
       this.add(panelButton, BorderLayout.SOUTH);
       
        // Tải dữ liệu khi khởi tạo
        loadReportData();
    }
    private void showReportChart() {
        Report latestReport = reportDAO.getLatestReport();
        if (latestReport != null) {
            // Tạo dataset cho biểu đồ
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Lịch hẹn chưa hoàn thành", latestReport.getCancelledAppointments());
            dataset.setValue("Lịch hẹn đã hoàn thành", latestReport.getCompletedAppointments());

            // Tạo biểu đồ
            JFreeChart chart = ChartFactory.createPieChart(
                    "Thống kê báo cáo",  // Tiêu đề biểu đồ
                    dataset,             // Dữ liệu
                    true,                // Hiển thị chú thích (legend)
                    true,                // Hiển thị tooltips
                    false                // Không cần URL
            );

            // Hiển thị biểu đồ trong một cửa sổ riêng
            ChartPanel chartPanel = new ChartPanel(chart);
            JFrame chartFrame = new JFrame("Biểu đồ thống kê");
            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            chartFrame.setContentPane(chartPanel);
            chartFrame.setSize(600, 400);
            chartFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để hiển thị biểu đồ!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void showBarChart() {
        Report latestReport = reportDAO.getLatestReport();
        if (latestReport != null) {
            // Tạo dataset cho biểu đồ
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.addValue(latestReport.getTotalPatients(), "Số liệu", "Tổng số bệnh nhân");
            dataset.addValue(latestReport.getTotalDoctors(), "Số liệu", "Tổng số bác sĩ");
            dataset.addValue(latestReport.getTodayAppointments(), "Số liệu", "Lịch hẹn hôm nay");
            dataset.addValue(latestReport.getCompletedAppointments(), "Số liệu", "Hoàn thành");

            // Tạo biểu đồ
            JFreeChart chart = ChartFactory.createBarChart(
                    "Thống kê báo cáo",  // Tiêu đề biểu đồ
                    "Danh mục",         // Trục X
                    "Số liệu",          // Trục Y
                    dataset,            // Dữ liệu
                    PlotOrientation.VERTICAL, // Hướng biểu đồ
                    true,               // Hiển thị chú thích (legend)
                    true,               // Hiển thị tooltips
                    false               // Không cần URL
            );

            // Hiển thị biểu đồ trong một cửa sổ riêng
            ChartPanel chartPanel = new ChartPanel(chart);
            JFrame chartFrame = new JFrame("Biểu đồ cột thống kê");
            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            chartFrame.setContentPane(chartPanel);
            chartFrame.setSize(800, 500);
            chartFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để hiển thị biểu đồ!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadReportData() {
        // Xóa dữ liệu cũ trong bảng
    	reportDAO.updateReport();
        tableModel.setRowCount(0);
        
        // Lấy báo cáo mới nhất từ CSDL
        Report latestReport = reportDAO.getLatestReport();
        if (latestReport != null) {
            tableModel.addRow(new Object[]{"Tổng số bệnh nhân", latestReport.getTotalPatients()});
            tableModel.addRow(new Object[]{"Tổng số bác sĩ", latestReport.getTotalDoctors()});
            tableModel.addRow(new Object[]{"Lịch hẹn hôm nay", latestReport.getTodayAppointments()});
            tableModel.addRow(new Object[]{"Lịch hẹn đã hoàn thành", latestReport.getCompletedAppointments()});
            tableModel.addRow(new Object[]{"Lịch hẹn chưa hoàn thành", latestReport.getCancelledAppointments()});
            tableModel.addRow(new Object[]{"Số hồ sơ được lập trong tháng", latestReport.getNewPatientsThisMonth()});
        } else {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu báo cáo!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
}

