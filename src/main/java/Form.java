import Serializers.Serializer;
import myArchitecture.*;
import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.ServiceLoader;


public class Form extends JFrame {

    private class SerializerWrapper implements Serializer{
        private Serializer wrappable;
        public SerializerWrapper(Serializer serializer) {
            this.wrappable = serializer;
        }

        @Override
        public String getName() {
            return wrappable.getName();
        }

        @Override
        public FileNameExtensionFilter getFilter() {
            return wrappable.getFilter();
        }

        @Override
        public boolean serialize(Object object, String pathname) throws IOException {
            return wrappable.serialize(object,pathname);
        }

        @Override
        public Object deSerialize(String pathname) throws IOException, ClassNotFoundException {
            return wrappable.deSerialize(pathname);
        }

        @Override
        public String toString() {
           return wrappable.getName();
        }
    }

    private class EncoderWrapper implements PlugTest
    {
        PlugTest wrapper;

        public EncoderWrapper(PlugTest wrappable) {
            this.wrapper=wrappable;

        }

        @Override
        public boolean checkFile(File src) {
            return wrapper.checkFile(src);
        }

        @Override
        public String getName() {
            return wrapper.getName();
        }

        @Override
        public String getExt() {
            return wrapper.getExt();
        }

        @Override
        public int encode(File dst, File src, String[] args) throws IOException {
            return wrapper.encode(dst,src,args);
        }

        @Override
        public int decode(File dst, File src, String[] args) throws IOException {
            return wrapper.decode(dst,src,args);
        }

        @Override
        public String toString() {
            return  wrapper.getName();
        }
    }

    public static ArrayList<Serializer> serializers = new ArrayList<>();
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JList objectList;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel rightPanel;
    private JButton serielizeButton;
    private JButton deserializeButton;
    private ArrayList arrayList = new ArrayList();

    private ArrayList<PlugTest> plugTests = new ArrayList<PlugTest>();


    public Form(int width, int height) throws HeadlessException {


        plugTests.add(new myDefaultenc());
        Main.plugins.forEach(o -> plugTests.add(o));



        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }


        add(panel1);
        setTitle("CRUD");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(width, height);
        setVisible(true);

//        objectList.addListSelectionListener((new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//
////                myPanel.currentEditable = arrayList.get(e.getFirstIndex());
//                myPanel.currentEditable = objectList.getSelectedValue();
//
//                repaintRight();
//            }
//        })
//       );

        arrayList.add(new Structure(new Point(0, 0), "structure1"));
        arrayList.add(new Office(new Point(20, 0), "office1", 3, 500));
        arrayList.add(new Streetlight(new Point(4, -50), "Streetlight1", false, 20));
        arrayList.add(new Cottage(new Point(24, -530), "Cottage1", 5, 20, 4, new Garage(new Point(24, -530), "Garage1", 5, 20)));

        objectList.setModel(new ListModel() {
            @Override
            public int getSize() {
                return arrayList.size();
            }

            @Override
            public Object getElementAt(int index) {
                return arrayList.get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {
            }

            @Override
            public void removeListDataListener(ListDataListener l) {
            }
        });


        objectList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                myPanel.currentEditable = arrayList.get(objectList.getSelectedIndex());
                repaintRight();

            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!objectList.isSelectionEmpty()) {
                    arrayList.remove(objectList.getSelectedIndex());

                    repaint();
                    objectList.clearSelection();
                } else {
                    JOptionPane.showMessageDialog(null, "Nothing to delete!");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object object = myPanel.currentEditable;
                Field[] fields = myPanel.getAllFields(object);
//                Field[] fields = object.getClass().getDeclaredFields();
                int i = 0;
                Component[] components = ((JPanel) rightPanel.getComponent(0)).getComponents();
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        if (myPanel.StringToObj(field.getType(), components[i + 1]) != null) {
                            field.set(
                                    object,
                                    myPanel.StringToObj(field.getType(), components[i + 1])
                            );
                        }
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                     catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,"NUMBER FORMAT EXCEPTION!");
//                    ex.printStackTrace();
                }
                    i += 2;
                }
                objectList.updateUI();
                objectList.revalidate();
                objectList.repaint();
            }


        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create();
            }
        });
        serielizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
              //TODO: CHANGE! <-changed

            //JComboBox jcd = new JComboBox(date);
                //     JOptionPane.showMessageDialog();
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                ArrayList<SerializerWrapper> serializerWrappers = new ArrayList<>();
                getSerializers().forEach(o->serializerWrappers.add(new SerializerWrapper(o)));
                JComboBox jcd = new JComboBox(serializerWrappers.toArray());
                if(0==JOptionPane.showConfirmDialog( null, jcd, "Date" ,JOptionPane.YES_OPTION))
                try {

                    FileNameExtensionFilter filter = ((Serializer)jcd.getSelectedItem()).getFilter();
                    // add filters
                    chooser.addChoosableFileFilter(filter);
                    chooser.setFileFilter(filter);


                    if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File tmp = new File("temp.tmp");
                        ((Serializer)jcd.getSelectedItem()).serialize(arrayList,tmp.getPath());
                        PlugTest pt=null;

                        ArrayList<EncoderWrapper> encoderWrappers = new ArrayList<>();
                        plugTests.forEach(o->encoderWrappers.add(new EncoderWrapper(o)));
                        JComboBox jcd2 = new JComboBox(encoderWrappers.toArray());
                        if(0==JOptionPane.showConfirmDialog( null, jcd2, "Date" ,JOptionPane.YES_OPTION))
                        {
                            pt= (PlugTest) jcd2.getSelectedItem();

//                       PlugTest pt =  new toDelete.myZBase32enc();


                       //+"."+pt.geExt()
                        pt.encode(new File(chooser.getSelectedFile().getAbsolutePath()),tmp,null);

                        }
                        tmp.deleteOnExit();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        deserializeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {


//
//
//                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
//
//
//                int returnValue = jfc.showOpenDialog(null);
//                if (returnValue == JFileChooser.APPROVE_OPTION) {
//
//
//                    try {
//                        arrayList = (ArrayList) new myXMLser().deSerialize(jfc.getSelectedFile().getAbsolutePath());
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    repaint();
//                    objectList.clearSelection();
//                }

                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                ArrayList<SerializerWrapper> serializerWrappers = new ArrayList<>();
                getSerializers().forEach(o->serializerWrappers.add(new SerializerWrapper(o)));
                JComboBox jcd = new JComboBox(serializerWrappers.toArray());
                if(0==JOptionPane.showConfirmDialog( null, jcd, "Date" ,JOptionPane.YES_OPTION))
                    try {

                        FileNameExtensionFilter filter = ((Serializer)jcd.getSelectedItem()).getFilter();
                        // add filters
                        chooser.addChoosableFileFilter(filter);
                        chooser.setFileFilter(filter);


                        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            PlugTest pt=null;
                            File tmp = new File("temp.tmp");
                           // System.out.println(pt.checkFile(new File(chooser.getSelectedFile().getAbsolutePath())));
                           // System.out.println(new toDelete.myBase64enc().checkFile(new File(chooser.getSelectedFile().getAbsolutePath())));

                            boolean found=false;
                            for (PlugTest plugTest : plugTests) {
                                if (plugTest.checkFile(new File(chooser.getSelectedFile().getAbsolutePath()))) {
                                    found = true;
                                    System.out.println(plugTest.getName());
                                    try {
                                        plugTest.decode(tmp, new File(chooser.getSelectedFile().getAbsolutePath()), null);

                                        arrayList = (ArrayList) ((Serializer)jcd.getSelectedItem()).deSerialize(tmp.getPath());
                                        tmp.deleteOnExit();
                                        objectList.updateUI();
                                        objectList.revalidate();
                                        objectList.repaint();
                                        repaint();
                                        objectList.clearSelection();

                                        break;
                                    } catch (IOException e) {
                                        JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().toString(), JOptionPane.ERROR_MESSAGE);
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if(!found)
                            {
                                JOptionPane.showMessageDialog(null, "no plugin found!","Error!", JOptionPane.ERROR_MESSAGE);
                            }

                        }


                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


            }
        });
    }

    private void create() {
        String s = JOptionPane.showInputDialog("Enter classname");

        try {

            Class clazz = Class.forName("myArchitecture." + s);
            Object object = myPanel.createObject(clazz);

            arrayList.add(object);
            objectList.updateUI();
            objectList.revalidate();
            objectList.repaint();


        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"Class not found!");
//                    ex.printStackTrace();

        } catch (IllegalAccessException| InstantiationException | InvocationTargetException ex) {
            JOptionPane.showMessageDialog(null,"Constructor exception!");
//                    ex.printStackTrace();
        }
    }

    public void repaintRight() {
        this.setTitle(myPanel.currentEditable.toString()+" ("+ myPanel.currentEditable.getClass() +")");

        rightPanel.removeAll();
        rightPanel.setBackground(Color.red); //means bad situations
        rightPanel.add(myPanel.generatePanel(myPanel.currentEditable));
        rightPanel.repaint();
        rightPanel.revalidate();

    }
    public static ArrayList<Serializer> getSerializers()
        {ArrayList<Serializer> serializers = new ArrayList<>();
            ServiceLoader<Serializer> loader = ServiceLoader.load(Serializer.class);
            for (Serializer s :loader ) { serializers.add(s); }
        return  serializers;
        }


}
