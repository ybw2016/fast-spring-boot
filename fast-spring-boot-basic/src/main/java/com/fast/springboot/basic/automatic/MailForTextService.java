package com.fast.springboot.basic.automatic;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * @author dev_jv
 * @since 2026-05-17
 */
public class MailForTextService {
    // 邮箱配置（改成你自己的）
    private static final String EMAIL = "TEST_MAIL";
    private static final String AUTH_CODE = "TEST_KEY"; // 不是密码！
    private static final String IMAP_HOST = "imap.qq.com";
    private static final int IMAP_PORT = 993;

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", IMAP_HOST);
        props.setProperty("mail.imap.port", String.valueOf(IMAP_PORT));
        props.setProperty("mail.imap.ssl.enable", "true");

        try {
            // 1. 创建会话
            Session session = Session.getDefaultInstance(props);
            // session.setDebug(true); // 开启调试日志

            // 2. 连接邮箱
            Store store = session.getStore();
            store.connect(EMAIL, AUTH_CODE);
            System.out.println("连接邮箱成功！");

            // 3. 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // 4. 获取邮件总数
            int total = inbox.getMessageCount();
            System.out.println("总邮件数：" + total);

            if (total == 0) {
                inbox.close(false);
                store.close();
                return;
            }

            // 5. 取最新 10 封
            int start = Math.max(1, total - 9);
            Message[] messages = inbox.getMessages(start, total);

            // 6. 遍历解析
            for (int i = 0; i < messages.length; i++) {
                Message msg = messages[i];

                System.out.println("===== 第 " + (i + 1) + " 封 =====");
                System.out.println("主题：" + msg.getSubject());
                System.out.println("发件人：" + getSender(msg));
                System.out.println("发送时间：" + msg.getSentDate());
                System.out.println("已读状态：" + msg.isSet(Flags.Flag.SEEN));
                System.out.println("邮件内容：\n" + getContent(msg));
                System.out.println("---------------------------------------\n");

                // 标记已读
                msg.setFlag(Flags.Flag.SEEN, true);
            }

            // 关闭
            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取发件人邮箱
     */
    private static String getSender(Message msg) throws Exception {
        Address[] from = msg.getFrom();
        if (from == null || from.length == 0) return "未知";
        return ((InternetAddress) from[0]).getAddress();
    }

    /**
     * 解析邮件正文（兼容文本/HTML/附件）
     * 1.4.5 专用写法
     */
    private static String getContent(Part part) throws Exception {
        // 纯文本 / HTML
        if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
            return (String) part.getContent();
        }

        // 多部件邮件（文本 + 附件）
        if (part.isMimeType("multipart/*")) {
            MimeMultipart mp = (MimeMultipart) part.getContent();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart bp = mp.getBodyPart(i);
                // 跳过附件，只解析正文
                if (!Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition())) {
                    sb.append(getContent(bp));
                }
            }
            return sb.toString();
        }
        return "无法解析内容";
    }
}