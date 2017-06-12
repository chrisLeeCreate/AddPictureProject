import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by lishaowei on 2017/6/12.
 */
public class DrawbleImmediatelyAction extends AnAction {

    private Project project;
    private String photoLocalPath;
    private String photoName;
    private String packageName = ""; //包名

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getData(PlatformDataKeys.PROJECT);
        packageName = getPackageName();
        initDialog();
        refreshProject(e);

    }

    /**
     * 刷新项目
     *
     * @param e
     */
    private void refreshProject(AnActionEvent e) {
        e.getProject().getBaseDir().refresh(false, true);
    }

    private void initDialog() {
        DrawableDialog drawableDialog = new DrawableDialog(new DrawableDialog.DialogCallBack() {
            @Override
            public void callBack(String photoPath, String photoNametextField) {
                System.out.println(photoPath);
                photoLocalPath = photoPath;
                photoName = photoNametextField;
                if (photoLocalPath.equals("")) {
                    Messages.showInfoMessage(project, "please enter a valid path", "warning");
                    return;
                }
                File file = new File(photoLocalPath);
                if (!file.exists()) {
                    Messages.showInfoMessage(project, "please enter a valid path", "warning");
                    return;
                }
                if (photoName.equals("")) {
                    Messages.showInfoMessage(project, "please enter a valid path", "warning");
                    return;
                }
                try {
                    checkLocalPhotoPath();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });
        drawableDialog.setVisible(true);
    }

    private void checkLocalPhotoPath() throws IOException {
        File file = new File(photoLocalPath);
        File[] files = file.listFiles();
        System.out.println(files.length);
        for (int i = 0; i < files.length; i++) {
            assert files != null;
            if (files[i].isDirectory()) {
                File[] filesOFabsoluteFile = files[i].listFiles();
                //old 办法
                //BufferedImage read = ImageIO.read(filesOFabsoluteFile[0]);
                String drawablePath = "drawable";
                switch (i) {
                    case 1: {
                        drawablePath = "drawable-hdpi";
                        break;
                    }
                    case 2: {
                        drawablePath = "drawable-mdpi";
                        break;
                    }
                    case 3: {
                        drawablePath = "drawable-xhdpi";
                        break;
                    }
                    case 4: {
                        drawablePath = "drawable-xxhdpi";
                        break;
                    }
                    case 5: {
                        drawablePath = "drawable-xxxhdpi";
                        break;
                    }
                }
                File fileCopy = new File(project.getBasePath() + "/app/src/main/res/" + drawablePath);
                if (!fileCopy.exists()) {
                    fileCopy.mkdirs();
                }
                //old 办法
                //ImageIO.write(read, "png", new File(project.getBasePath() + "/app/src/main/res/" + drawablePath + "/" + photoName + ".png"));

                File photoNameFile = new File(project.getBasePath() + "/app/src/main/res/" + drawablePath + "/" + photoName + ".png");
                if (photoNameFile.exists()) {
                    Messages.showInfoMessage(project, "this is a exist photoName,pleace change a new name", "warning");
                    return;
                }
                Files.copy(filesOFabsoluteFile[0].toPath(), new File(project.getBasePath() + "/app/src/main/res/" + drawablePath + "/" + photoName + ".png").toPath());

            }
        }
    }

    /**
     * 从AndroidManifest.xml文件中获取当前app的包名
     *
     * @return
     */

    private String getPackageName() {
        String package_name = "";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(project.getBasePath() + "/App/src/main/AndroidManifest.xml");

            NodeList nodeList = doc.getElementsByTagName("manifest");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element element = (Element) node;
                package_name = element.getAttribute("package");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return package_name;
    }
}
