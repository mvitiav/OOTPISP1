package Form;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;


public class myPanel {
//    public myPanel() {
//        setLayout(new java.awt.GridLayout(4, 4));
//        for (int i = 0; i < 16; ++i) {
//            JButton b = new JButton(String.valueOf(i));
//            b.addActionListener(new java.awt.event.ActionListener() {
//                public void actionPerformed(java.awt.event.ActionEvent e) {
//                    //...
//                }
//            });
//            add(b);
//        }
//    }

    public static  Field[] getAllFields(Object object)

    {
        ArrayList<Field> fields = new ArrayList<Field>();
        Class clazz =object.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return ( Field[])fields.toArray(new Field[fields.size()]);

    }

    public static Object StringToObj(Class clazz, Component component)
    {
        if (clazz==Boolean.class){return ((JCheckBox)component).isSelected();}
        if (clazz.isAssignableFrom(Enum.class)||(clazz==Enum.class)){return ((JComboBox)component).getSelectedIndex();}
        if ((clazz==Integer.class)||(clazz==int.class)){return Integer.parseInt(((JTextArea)component).getText());}
        if (clazz==Double.class){return Double.parseDouble(((JTextArea)component).getText());}
            return ((JTextArea)component).getText();
    }

    public static JPanel generatePanel(Object object) {
        JPanel panel = new JPanel(null);
        panel.setLayout(new java.awt.GridLayout(4, 4));
        Field[] fields = getAllFields(object);
        for(Field field:fields){
          field.setAccessible(true);
            panel.add(new JLabel(field.getName()));
            try {
                Class clazz =object.getClass();
                if (field.getType()==Boolean.class){
                    JCheckBox jCheckBox=  new JCheckBox();
                    jCheckBox.setSelected((Boolean)field.get(object));
                    panel.add(jCheckBox);
                }else
//                if (field.getType().isAssignableFrom(Enum.class)||(clazz==Enum.class)){
//               //  JComboBox jComboBox = new JComboBox( ((Enum)field.get(object)));
//
//
//                    JComboBox jComboBox = new JComboBox
//                   // ((Enum)field.get(object)).to
//                }

                if ((field.getType()==String.class)||(field.getType()==Double.class) ||(field.getType()==Integer.class) ||(field.getType()==int.class) )  {  panel.add(new JTextArea(String.valueOf(field.get(object))));}
                else
                {
                    panel.add(new JButton(String.valueOf(field.get(object))));
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return panel;
    }
}
