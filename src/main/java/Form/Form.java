package Form;

import myArchitecture.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import java.lang.reflect.*;

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
        arrayList.add(new Structure(new Point(0,0),"structure1"));
        arrayList.add(new Office(new Point(20,0),"office1",3,500));
        arrayList.add(new Streetlight(new Point(4,-50),"Streetlight1",false,20));
        arrayList.add(new Cottage(new Point(24,-530),"Cottage1",5,20,4,new Garage(new Point(24,-530),"Garage1",5,20)));

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
            public void mouseClicked(MouseEvent e)
            {
                System.out.println("clicked!");
                //rightPanel=myPanel.generatePanel( arrayList.get(objectList.getSelectedIndex()));
              //  rightPanel.add(new JLabel("12123"));
                rightPanel.removeAll();
                rightPanel.setBackground(Color.red);

                    rightPanel.add(myPanel.generatePanel( arrayList.get(objectList.getSelectedIndex())));


                rightPanel.repaint();
                rightPanel.revalidate();



            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                arrayList.remove(objectList.getSelectedIndex());

                repaint();
                objectList.clearSelection();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Object object= arrayList.get(objectList.getSelectedIndex());
                Field[] fields =   myPanel.getAllFields(object);
//                Field[] fields = object.getClass().getDeclaredFields();
                int i=0;
                Component[] components = ((JPanel)rightPanel.getComponent(0)).getComponents();
                for(Field field:fields){
                    field.setAccessible(true);
                    try {

                        field.set(
                                object,
                                myPanel.StringToObj(field.getType(),components[i+1])
                        );
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                    i+=2;
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
