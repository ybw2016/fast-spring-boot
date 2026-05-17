package com.fast.springboot.basic.automatic;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author dev_jv
 * @since 2026-05-17
 */
public class MailForErWeiMaAttachService {
    // 邮箱配置（改成你自己的）
    private static final String EMAIL = "TEST_MAIL";
    private static final String AUTH_CODE = "TEST_KEY"; // 不是密码！
    private static final String IMAP_HOST = "imap.qq.com";
    private static final int IMAP_PORT = 993;
    private static final String SAVE_IMAGE_PATH = "LOCAL_PATH"; // 图片保存路径

    public static void main(String[] args) {
        new File(SAVE_IMAGE_PATH).mkdirs();

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap.qq.com");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.ssl.enable", "true");

        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore();
            store.connect(EMAIL, AUTH_CODE);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();

            if (messages.length > 0) {
                Message msg = messages[messages.length - 1];
                System.out.println("主题：" + MimeUtility.decodeText(msg.getSubject()));
                parseInlineImage(msg);
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 核心：递归读取 邮件正文内嵌图片（非附件）
     */
    private static void parseInlineImage(Part part) throws Exception {
        if (part.isMimeType("multipart/*")) {
            MimeMultipart mm = (MimeMultipart) part.getContent();
            for (int i = 0; i < mm.getCount(); i++) {
                BodyPart bp = mm.getBodyPart(i);
                parseInlineImage(bp);
            }
            return;
        }

        // ================== 关键：识别正文图片 ==================
        if (part instanceof MimeBodyPart) {
            MimeBodyPart mbp = (MimeBodyPart) part;
            String cid = mbp.getContentID(); // 内嵌图片一定有 cid
            String disp = mbp.getDisposition();

            // 正文图片 = 有cid + 内联(inline)
            if (cid != null && (disp == null || "inline".equalsIgnoreCase(disp))) {
                if (mbp.isMimeType("image/*")) {
                    System.out.println("找到正文图片，CID：" + cid);

                    // 读取图片
                    BufferedImage image = ImageIO.read(mbp.getInputStream());

                    // 解析二维码
                    String qrContent = decodeQR(image);
                    System.out.println("二维码内容：" + qrContent);

                    // 解析发票信息
                    if (qrContent != null) {
                        parseInvoiceQR(qrContent);
                    }
                }
            }
        }
    }

    /**
     * 二维码解码
     */
    private static String decodeQR(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析国家税务总局新版电子发票二维码
     */
    private static void parseInvoiceQR(String qrText) {
        System.out.println("\n============= 发票二维码解析结果 =============");

        // 标准格式：01,发票代码,发票号码,开票日期,校验码,金额,...
        String[] arr = qrText.split(",");
        if (arr.length >= 6) {
            System.out.println("发票代码：" + arr[1]);
            System.out.println("发票号码：" + arr[2]);
            System.out.println("开票日期：" + arr[3]);
            System.out.println("校验码：" + arr[4]);
            System.out.println("发票金额：" + arr[5]);
        } else {
            parseInvoiceQRFromRawLinkByExt1(qrText);
        }
        System.out.println("==============================================");
    }

    public static void parseInvoiceQRFromRawLinkByExt1(String rawLinkUrl) {
        // 自动配置 Chrome 驱动
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 后台运行，不显示窗口
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-javascript");

        WebDriver driver = new ChromeDriver(options);
        // driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        try {
            driver.get(rawLinkUrl);
            Thread.sleep(2000); // 等待页面加载
            String pageText = driver.findElement(By.tagName("body")).getText();

            System.out.println("===== 发票完整信息 =====");
            System.out.println("发票号码：" + extract(pageText, "2621\\d{14}"));
            System.out.println("购买方名称：" + extract(pageText, "徐鹏.*?"));
            System.out.println("购买方纳税人识别号：" + extract(pageText, "\\b\\d{17}[\\dX]\\b"));
            System.out.println("销售方：" + extract(pageText, "盛京银行.*?"));
            System.out.println("价税合计：" + extract(pageText, "76\\.08"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public static void parseInvoiceQRFromRawLinkByJar(String rawLinkUrl) {
        // 你的发票链接
        try (WebClient webClient = new WebClient()) {
            // 关闭各种报错，支持JS渲染
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            // webClient.getOptions().setDownloadImages(false);

            // 等待页面JS执行完成
            HtmlPage page = webClient.getPage(rawLinkUrl);
            webClient.waitForBackgroundJavaScript(3000); // 等待3秒加载数据
            // String pageText = page.asText();
            String text = page.getWebResponse().getContentAsString();

            // 开始解析
            System.out.println("======= 数电票解析结果 =======");
            System.out.println("发票号码：" + get(text, "2621\\d{14}"));
            System.out.println("开票日期：" + get(text, "2026\\-\\d{2}\\-\\d{2}"));
            System.out.println("购买方：" + get(text, "徐鹏.*?"));
            System.out.println("销售方：" + get(text, "盛京银行.*?"));
            System.out.println("价税合计：" + get(text, "76\\.08"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 最简单的正则提取，绝对不报错
    private static String get(String text, String regex) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : "未识别";
    }

    /**
     * 此方法会报错
     *
     * @param rawLinkUrl
     */
    public static void parseInvoiceQRFromRawLink(String rawLinkUrl) {
        // 你要解析的发票URL
        try {
            // 1. 访问发票页面并获取文本
            Document doc = Jsoup.connect(rawLinkUrl).get();
            String text = doc.text();

            // 2. 解析发票信息
            String invoiceType = extract(text, "电子发票（(.*?)）");
            String invoiceNum = extract(text, "数电发票号码[:：]\\s*([\\dA-Z]+)");
            String invoiceDate = extract(text, "开票日期[:：]\\s*(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})");
            String buyerName = extract(text, "购买方名称[:：]\\s*([^\\n]+)");
            String buyerId = extract(text, "购买方纳税人识别号[:：]\\s*([^\\n]+)");
            String sellerName = extract(text, "销售方名称[:：]\\s*([^\\n]+)");
            String sellerId = extract(text, "销售方纳税人识别号[:：]\\s*([^\\n]+)");
            String totalPrice = extract(text, "价税合计.*?[:：]\\s*[￥]?([\\d.]+)");

            // 3. 输出结果
            System.out.println("===== 数电票解析结果 =====");
            System.out.println("发票类型：" + invoiceType);
            System.out.println("数电发票号码：" + invoiceNum);
            System.out.println("开票日期：" + invoiceDate);
            System.out.println("购买方名称：" + buyerName);
            System.out.println("购买方识别号：" + buyerId);
            System.out.println("销售方名称：" + sellerName);
            System.out.println("销售方识别号：" + sellerId);
            System.out.println("价税合计：" + totalPrice + " 元");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 正则抽取工具
    private static String extract(String text, String regex) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(regex).matcher(text);
        return m.find() ? m.group(1).trim() : "未识别";
    }
}