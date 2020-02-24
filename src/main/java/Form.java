import myArchitecture.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public class Form extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JList objectList;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel rightPanel;
    private ArrayList arrayList = new ArrayList();


    public Form(int width, int height) throws HeadlessException {

        add(panel1);
        setTitle("CRD");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(width, height);
        setVisible(true);
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
        });
    }

    public void repaintRight() {
        this.setTitle(myPanel.currentEditable.toString()+" ("+String.valueOf(myPanel.currentEditable.getClass())+")");

        rightPanel.removeAll();
        rightPanel.setBackground(Color.red); //means bad situations
        rightPanel.add(myPanel.generatePanel(myPanel.currentEditable));
        rightPanel.repaint();
        rightPanel.revalidate();

    }

}
