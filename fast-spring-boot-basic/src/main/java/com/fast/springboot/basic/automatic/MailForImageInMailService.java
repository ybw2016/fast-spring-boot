package com.fast.springboot.basic.automatic;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * @author dev_jv
 * @since 2026-05-17
 */
@Deprecated
public class MailForImageInMailService {
    // ================== 请修改这里 ==================
    private static final String USERNAME = "TEST_MAIL";
    private static final String PASSWORD = "TEST_KEY"; // 不是密码！
    private static final String SAVE_DIR = "LOCAL_PATH"; // 图片保存路径
    // ================================================

    public static void main(String[] args) {
        new File(SAVE_DIR).mkdirs();

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap.qq.com");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.ssl.enable", "true");

        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore();
            store.connect(USERNAME, PASSWORD);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            System.out.println("总邮件数：" + messages.length);

            // 只读取最新1封
            if (messages.length > 0) {
                Message lastMessage = messages[messages.length - 1];
                System.out.println("主题：" + MimeUtility.decodeText(lastMessage.getSubject()));
                parseMessage(lastMessage);
            }

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 解析邮件
    public static void parseMessage(Part part) throws Exception {
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                Part bodyPart = multipart.getBodyPart(i);
                parseMessage(bodyPart);
            }
        } else {
            // 关键：只有 MimeBodyPart 才有图片信息
            if (part instanceof MimeBodyPart) {
                MimeBodyPart mbp = (MimeBodyPart) part;

                // 内嵌图片一定有 Content-ID
                String cid = mbp.getContentID();
                // if (cid != null) {
                saveImage(mbp, cid);
                // }
            }
        }
    }

    // 保存图片
    private static void saveImage(MimeBodyPart mbp, String cid) throws Exception {
        // cid = cid.replaceAll("[<>]", "");
        // String fileName = cid + ".png";
        String fileName = UUID.randomUUID().toString() + ".png";
        File file = new File(SAVE_DIR + fileName);

        try (InputStream in = mbp.getInputStream();
             FileOutputStream out = new FileOutputStream(file)) {

            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }

        System.out.println("✅ 图片已保存：" + file.getAbsolutePath());
    }

    private static String getSender(Message msg) throws Exception {
        Address[] from = msg.getFrom();
        return from == null ? "未知" : ((InternetAddress) from[0]).getAddress();
    }
}
