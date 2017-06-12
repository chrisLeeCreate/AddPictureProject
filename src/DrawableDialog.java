import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class DrawableDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField photoPathTextField;
    private JTextField photoNametextField;
    private DialogCallBack dialogCallBack;

    public DrawableDialog(DialogCallBack dialogCallBack) {
        this.dialogCallBack = dialogCallBack;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(640, 360);
        setLocationRelativeTo(null);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        if (dialogCallBack != null) {
            dialogCallBack.callBack(photoPathTextField.getText().trim(), photoNametextField.getText().trim());
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        DrawableDialog dialog = new DrawableDialog(new DialogCallBack() {
            @Override
            public void callBack(String photoPath, String photoNametextField) {
                System.out.println(photoPath);


                File file = new File(photoPath);
                File[] files = file.listFiles();
                System.out.println(files.length);
                System.out.println(file.length());
                for (int i = 0; i < files.length; i++) {
                    assert files != null;
                    if (files[i].isDirectory()) {
                        File[] filesOFabsoluteFile = files[i].listFiles();
                        System.out.println(files[i].getAbsoluteFile());

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
                        System.out.println(drawablePath);
                    }
                }
            }
        });
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public interface DialogCallBack {
        void callBack(String photoPath, String photoNametextField);
    }

}
