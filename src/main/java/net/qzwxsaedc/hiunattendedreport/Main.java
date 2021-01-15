package net.qzwxsaedc.hiunattendedreport;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.ExternalResource;
import net.qzwxsaedc.hiunattendedreport.event.GetEvent;
import net.qzwxsaedc.hiunattendedreport.event.JVMShutdownEvent;
import net.qzwxsaedc.hiunattendedreport.event.PostEvent;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.Map;
import java.util.Scanner;

import static net.qzwxsaedc.hiunattendedreport.Misc.getHtmlPage;
import static net.qzwxsaedc.hiunattendedreport.HttpServiceHandler.*;

public class Main {
    static final Base64.Decoder decoder = Base64.getUrlDecoder();
    public static void main(String[] args){
        User user = User.UserFactory();

        final Bot bot = BotFactory.INSTANCE.newBot(user.getQQ(), user.getPassword(), new BotConfiguration() {
            {
                fileBasedDeviceInfo();
                setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
                redirectBotLogToFile();
                redirectBotLogToDirectory();
                redirectNetworkLogToFile();
                redirectBotLogToDirectory();
            }
        });

        bot.login();
        HttpService service = new HttpService();

        GetEvent.register("/", (ctx, req) -> {
            String html = getHtmlPage("index.html");
            System.out.println("访问主页");
            HttpServiceHandler.send(ctx, html, "text/html");
        });
        GetEvent.register("/login", ((ctx, req) -> {
            System.out.println("访问登录接口");
            HttpServiceHandler.send(ctx, "{\"code\":1100,\"content\":[\"该接口已停用\"]}");
        }));
        GetEvent.register("/logout", ((ctx, req) -> {
            System.out.println("访问登出接口");
            HttpServiceHandler.send(ctx, "{\"code\":1100,\"content\":[\"该接口已停用\"]}");
        }));

        PostEvent.register("/send_img", ((ctx, req) -> {
            Map<String, Object> arg = getRequestParams(req);
            if(!(arg.containsKey("group") && (arg.containsKey("img") || arg.containsKey("url")))){
                send(ctx, "{\"code\":1101,\"content\":[\"参数错误。\"]}");
                return;
            }
            long group = Long.parseLong(arg.get("group").toString());

            Group target = bot.getGroup(group);
            if(target == null){
                bot.close(new NullPointerException("未加入该群"));
                send(ctx, "{\"code\":1200,\"content\":[\"参数错误。\"]}");
                return;
            }

            if(arg.containsKey("img")){
                final byte[] buffer = decoder.decode((String) arg.get("img"));
                File tmp_path = new File("tmp");
                if(!tmp_path.exists())  tmp_path.mkdir();
                File tmp_img = new File("tmp" + File.separator + MD5.cal(buffer) + ".png");

                if(!tmp_img.exists()){
                    DataOutputStream output = new DataOutputStream(new FileOutputStream(tmp_img));
                    output.write(buffer);
                    output.flush();
                    output.close();
                }
                final Image image = target.uploadImage(ExternalResource.create(tmp_img));
                target.sendMessage(image);
            }else{
                try{
                    URL url = new URL(arg.get("url").toString());
                    final Image image = target.uploadImage(ExternalResource.create(url.openConnection().getInputStream()));
                    target.sendMessage(image);
                }catch (IOException e){
                    e.printStackTrace();
                    send(ctx, "{\"code\":1102,\"content\":[\"URL指向的资源加载失败。\"]}");
                    return;
                }
            }

            send(ctx, "{\"code\":0,\"content\":[\"发送成功。\"]}");
        }));

        JVMShutdownEvent.register(() -> {
            bot.close(null);
            System.out.println("bot logout.");
        });

        try {
            service.start(6291);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("启动完成");
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while(!scanner.nextLine().equals("stop"));
            System.exit(0);
        }).start();
        try {
            service.block();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
