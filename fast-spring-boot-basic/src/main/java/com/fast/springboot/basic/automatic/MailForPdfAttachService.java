package com.fast.springboot.basic.automatic;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.mail.*;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dev_jv
 * @since 2026-05-17
 */
public class MailForPdfAttachService {
    // 邮箱配置（改成你自己的）
    private static final String EMAIL = "TEST_MAIL";
    private static final String AUTH_CODE = "TEST_KEY"; // 不是密码！
    private static final String IMAP_HOST = "imap.qq.com";
    private static final int IMAP_PORT = 993;
    private static final String SAVE_IMAGE_PATH = "LOCAL_PATH"; // 图片保存路径

    public static void main(String[] args) {
        // 创建图片保存目录
        new File(SAVE_IMAGE_PATH).mkdirs();

        // IMAP 配置（1.4.5 通用）
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap.qq.com");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.ssl.enable", "true");

        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore();
            store.connect(EMAIL, AUTH_CODE);

            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            int total = inbox.getMessageCount();
            System.out.println("总邮件数：" + total);

            // 取最新10封
            int start = Math.max(1, total - 9);
            Message[] messages = inbox.getMessages(start, total);

            for (Message msg : messages) {
                System.out.println("======================================");
                System.out.println("主题：" + MimeUtility.decodeText(msg.getSubject()));

                // 解析邮件 + 提取图片
                handleAttachment(msg);
                System.out.println("图片保存完成\n");
            }

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== 解析邮件附件 =====================
    private static void handleAttachment(Part part) throws Exception {
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                handleAttachment(mp.getBodyPart(i));
            }
        }

        // 判断附件
        String disp = part.getDisposition();
        if (Part.ATTACHMENT.equalsIgnoreCase(disp)) {
            String fileName = MimeUtility.decodeText(part.getFileName());
            if (fileName.toLowerCase().endsWith(".pdf")) {
                System.out.println("找到PDF发票：" + fileName);

                // 保存PDF
                File pdfFile = new File(SAVE_IMAGE_PATH + fileName);
                try (InputStream in = part.getInputStream();
                     FileOutputStream out = new FileOutputStream(pdfFile)) {
                    IOUtils.copy(in, out);
                }
                System.out.println("PDF已保存：" + pdfFile.getAbsolutePath());

                // 2. 解析PDF文本
                String pdfText = extractPdfText(part.getInputStream());

                // 3. 提取发票关键信息
                // parseInvoice(pdfText);
                parseIdCard(pdfText);

                // 识别二维码
//                String qrContent = detectQRCodeFromPDF(pdfFile);
//                System.out.println("二维码内容：" + qrContent);
//
//                // 解析发票信息
//                if (qrContent != null) {
//                    parseInvoiceFromQR(qrContent);
//                }
            }
        }
    }

    // 3.1 PDF转图片 → 识别二维码
    private static String extractPdfText(InputStream inputStream) {
        try (PDDocument doc = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        } catch (Exception e) {
            return null;
        }
    }

    private static final Pattern ID_CARD_PATTERN = Pattern.compile("([1-9]\\d{5})((?:18|19|20)\\d{2})(?:0[1-9]|10|11|12)((?:0[1-9]|[1-2]\\d|30|31)\\d{3})([\\dXx])");

    private static void parseIdCard(String pdfText) {
        Matcher matcher = ID_CARD_PATTERN.matcher(pdfText);
        if (matcher.find()) {
            String idCard = matcher.group();
            System.out.println("匹配的身份证号：" + idCard);
        }
    }

    /**
     * 解析发票：自动提取 发票代码、号码、日期、购买方、税号、金额、商品明细
     */
    private static void parseInvoice(String pdfText) {
        System.out.println("\n==================== 【发票解析结果】 ====================");

        // 1. 去除多余空白，方便匹配
        pdfText = pdfText.replaceAll("\\s+", " ").trim();

        // --------------------- 1. 提取基础信息 ---------------------
        String invoiceCode = regex(pdfText, "发票代码[:：]?\\s*(\\d{10,12})");          // 发票代码
        String invoiceNum = regex(pdfText, "发票号码[:：]?\\s*(\\d{8,10})");           // 发票号码
        String invoiceDate = regex(pdfText, "(开票|开具)日期[:：]?\\s*(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})"); // 日期
        String buyerName = regex(pdfText, "购买方[:：]?.*?名称[:：]?\\s*([^\\s]+?)(?=\\s*纳税人|$)");    // 购买方名称
        String buyerTaxId = regex(pdfText, "纳税人识别号[:：]?\\s*([A-Z0-9]{15,20})"); // 税号
        // String totalPrice = regex(pdfText, "价税合计[:：]?[^0-9]*?(\\d+\\.?\\d*)");   // 价税合计

        // 打印基础信息
        System.out.println("发票代码：" + invoiceCode);
        System.out.println("发票号码：" + invoiceNum);
        System.out.println("开票日期：" + invoiceDate);
        System.out.println("购买方：" + buyerName);
        System.out.println("税号：" + buyerTaxId);
        // System.out.println("价税合计：" + totalPrice + " 元");

        // --------------------- 2. 提取发票商品/服务明细（核心！） ---------------------
        System.out.println("\n---------------- 【发票明细】 ----------------");
        // 匹配：名称 规格 单位 数量 单价 金额 税率 税额
        String itemRegex = "(?![价税合计|小写|大写])"
                + "([^\\s]+?)\\s+"        // 商品名称
                + "([^\\s]*)\\s*"         // 规格型号
                + "([^\\s]*)\\s*"         // 单位
                + "([\\d.]+)\\s*"         // 数量
                + "([\\d.]+)\\s*"         // 单价
                + "([\\d.]+)\\s*"         // 金额
                + "([\\d.]+%?)\\s*"       // 税率
                + "([\\d.]+)";            // 税额

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(itemRegex);
        java.util.regex.Matcher matcher = pattern.matcher(pdfText);

        int index = 1;
        boolean hasItem = false;
        while (matcher.find()) {
            hasItem = true;
            String name = matcher.group(1).trim();
            String spec = matcher.group(2).trim();
            String unit = matcher.group(3).trim();
            String count = matcher.group(4).trim();
            String price = matcher.group(5).trim();
            String amount = matcher.group(6).trim();
            String tax = matcher.group(7).trim();
            String taxAmount = matcher.group(8).trim();

            System.out.println("第" + (index++) + "行：");
            System.out.println("  商品：" + name);
            System.out.println("  规格：" + spec);
            System.out.println("  单位：" + unit);
            System.out.println("  数量：" + count);
            System.out.println("  单价：" + price);
            System.out.println("  金额：" + amount);
            System.out.println("  税率：" + tax);
            System.out.println("  税额：" + taxAmount);
            System.out.println("----------------------------------------");
        }

        if (!hasItem) {
            // 备用简单匹配（适配部分PDF格式）
            String simpleItemRegex = "([^\\s]+)\\s+([\\d.]+)\\s+([\\d.]+)\\s+([\\d.]+)";
            matcher = java.util.regex.Pattern.compile(simpleItemRegex).matcher(pdfText);
            index = 1;
            while (matcher.find()) {
                System.out.println("明细" + (index++) + "："
                        + matcher.group(1) + " | "
                        + matcher.group(2) + " | "
                        + matcher.group(3) + " | "
                        + matcher.group(4));
            }
        }

        System.out.println("=======================================================\n");
    }

    /**
     * 正则抽取工具方法
     */
    private static String regex(String text, String regex) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(regex).matcher(text);
        return m.find() ? m.group(2) != null ? m.group(2) : m.group(1) : "未识别";
    }
}