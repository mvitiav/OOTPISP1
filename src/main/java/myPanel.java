import myArchitecture.Garage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;


public class myPanel {

    public static Object currentEditable;

    public static Object createObject(Class clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {

        Object object =null;

        if ((clazz==Integer.class)||(clazz==int.class)||(clazz==double.class)||(clazz==float.class))
        {
            return 0;
        }
        if ((clazz==Boolean.class))
        {
            return false;
        }
        if ((clazz==String.class))
        {
            return "sample_text";
        }

        Constructor constructor=null;
        if(clazz.getConstructors().length>1){
        String[] options = { "OK", "Cancel"};
        JComboBox jcb = new JComboBox(clazz.getConstructors());
         constructor = (Constructor) jcb.getSelectedItem();
        int selection = JOptionPane.showOptionDialog(null, jcb, "Constructor selection:",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
                options[0]);}else constructor=clazz.getConstructors()[0];
        Object[] zeroes = new Object[constructor.getParameterCount()];
        Class[] types = constructor.getParameterTypes();

        for(int i=0;i<types.length;i++)
        {
            zeroes[i]=createObject(types[i]);
        }

        object = constructor.newInstance(zeroes);
                return object;
    }

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
        if (clazz instanceof Class && ((Class<?>)clazz).isEnum()){return ((JComboBox)component).getSelectedIndex();}
        if ((clazz==Integer.class)||(clazz==int.class)){return Integer.parseInt(((JTextArea)component).getText());}
        if (clazz==Double.class){return Double.parseDouble(((JTextArea)component).getText());}
         if(clazz==String.class)  { return ((JTextArea)component).getText();}
         return null;
    }

    public static JPanel generatePanel(Object object) {
        JPanel panel = new JPanel(null);
        panel.setLayout(new java.awt.GridLayout(10, 2));
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
//                if (field.getType() instanceof Class && ((Class<?>)field.getType()).isEnum()){
//               //  JComboBox jComboBox = new JComboBox( ((Enum)field.get(object)));
//
//                    JComboBox jComboBox = new JComboBox(getEnumValues());
//                   // ((Enum)field.get(object)).to
//                    panel.add(jComboBox);
//                }

                if ((field.getType()==String.class)||(field.getType()==Double.class) ||(field.getType()==Integer.class) ||(field.getType()==int.class) )  {  panel.add(new JTextArea(String.valueOf(field.get(object))));}
                else
                {
                    JButton jButton = new JButton(String.valueOf(field.get(object)));
                    jButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                currentEditable = field.get(object);
                            Main.form.repaintRight();
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    panel.add(jButton);
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return panel;
    }

}
