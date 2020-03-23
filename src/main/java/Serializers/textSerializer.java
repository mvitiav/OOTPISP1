package Serializers;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

public class textSerializer implements Serializer {
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();
    private FileWriter fileWriter;
    private FileReader fileReader;
    private Scanner scanner;

    private static Field[] getAllFields(Object object) {
        int a = 5;
        ArrayList<Field> fields = new ArrayList<>();
        Class clazz = object.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        fields.forEach(field -> field.setAccessible(true));
        return fields.toArray(new Field[fields.size()]);

    }

    private static Object stringToType(String string, Class<?> clazz) {
        if (clazz==Boolean.class){return false;}
        PropertyEditor editor = PropertyEditorManager.findEditor(clazz);
        editor.setAsText(string);
        return editor.getValue();
        // return null;
    }

    private static boolean isWrapperType(Class<?> clazz) {
        return (clazz == String.class || clazz.isPrimitive() || WRAPPER_TYPES.contains(clazz));
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }

    @Override
    public String getName() {
        return "textSerializer";
    }

    @Override
    public FileNameExtensionFilter getFilter() {
        return new FileNameExtensionFilter("txt files (*.txt)", "txt");
    }

    @Override
    public boolean serialize(Object object, String pathname) throws IOException {


//        fileWriter.write("123");
//        fileWriter.write("321");

        try {
            fileWriter = new FileWriter(pathname);
            fileWriter.write('{' + String.valueOf(object.getClass().getName()) + '}' + '\n');
            objToString(object, 1, "Object");
            fileWriter.write('{' + String.valueOf(object.getClass().getName()) + '}' + '\n');
        } finally {
            fileWriter.close();
        }


        return false;
    }

    @Override
    public String toString() {
        return "textSerializer{}";
    }

    private String[] objToString(Object object, int lvl, String fieldStr) throws IOException {
        char[] repeat = new char[lvl];
        //  Arrays.fill(repeat, ' ');
//        System.out.print(new String(repeat)+fieldStr+':');
//        System.out.println();
        // fileWriter.write(new String(repeat)+fieldStr+':'+'\n');

        if (object == null) {
            //System.out.println("null");
            fileWriter.write("null\n");
        } else
            if(object instanceof Collection<?>)
            {  repeat = new char[lvl];
                Arrays.fill(repeat, ' ');
                //System.out.print(new String(repeat)+field.getName()+':');
                fileWriter.write(new String(repeat) + '<' + object.getClass().getName() + '>' + '\n');
                lvl++;
                for (Object o : ((Collection<?>) object)) {
                    repeat = new char[lvl];
                    Arrays.fill(repeat, ' ');
                    //System.out.print(new String(repeat)+field.getName()+':');
                    fileWriter.write(new String(repeat) + '{' + o.getClass().getName() + '}' + '\n');
                   // lvl++;
                    objToString(o, lvl +1, "???");
                    //lvl--;
                    repeat = new char[lvl];
                    Arrays.fill(repeat, ' ');
                    //System.out.print(new String(repeat)+field.getName()+':');
                    fileWriter.write(new String(repeat) + '{' +o.getClass().getName() + '}' + '\n');

                }
                lvl--;
                repeat = new char[lvl];
                Arrays.fill(repeat, ' ');
                //System.out.print(new String(repeat)+field.getName()+':');
                fileWriter.write(new String(repeat) + '<' + object.getClass().getName() + '>' + '\n');

            }else
            for (Field field : getAllFields(object)) {
//            field.setAccessible(true);

                repeat = new char[lvl];
                Arrays.fill(repeat, ' ');
                //System.out.print(new String(repeat)+field.getName()+':');
                fileWriter.write(new String(repeat) + '{' + field.getType().getName() + '}' + '\n');
                lvl++;

                if (isWrapperType(field.getType())) {
                    repeat = new char[lvl];
                    Arrays.fill(repeat, ' ');
                    //System.out.print(new String(repeat)+field.getName()+':');
                    fileWriter.write(new String(repeat) + ":" + field.getName() + ':');
                    try {
                        // System.out.println(field.get(object));
                        fileWriter.write(String.valueOf(field.get(object)) + '\n');
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {

                    repeat = new char[lvl];
                    Arrays.fill(repeat, ' ');
                    //System.out.print(new String(repeat)+field.getName()+':');
                    fileWriter.write(new String(repeat) + ";" + field.getName() + ";\n");

//                    System.out.println();
                    //    fileWriter.write('\n');
                    try {
                        Field field1 = object.getClass().getDeclaredField(field.getName());
                        field1.setAccessible(true);
                        Object obj = field1.get(object);

                        if (field1.getType().isArray() && !field1.getType().getComponentType().isPrimitive()) {
                            Object[] fs = (Object[]) obj;
                            int i = 0;
                            for (Object fi : fs) {

                                repeat = new char[lvl + 1];
                                Arrays.fill(repeat, ' ');

                                fileWriter.write(new String(repeat) + '(' + i + ')' + '\n');

                                if (fi != null) {
                                    repeat = new char[lvl + 1];
                                    Arrays.fill(repeat, ' ');
                                    //System.out.print(new String(repeat)+field.getName()+':');
                                    fileWriter.write(new String(repeat) + '{' + fi.getClass().getName() + '}' + '\n');
                                }

                                objToString(fi, lvl + 2, String.valueOf(i));

                                if (fi != null) {
                                    repeat = new char[lvl + 1];
                                    Arrays.fill(repeat, ' ');
                                    //System.out.print(new String(repeat)+field.getName()+':');
                                    fileWriter.write(new String(repeat) + '{' + fi.getClass().getName() + '}' + '\n');
                                }

                                repeat = new char[lvl + 1];
                                Arrays.fill(repeat, ' ');
                                fileWriter.write(new String(repeat) + '(' + i++ + ')' + '\n');
                            }
                        } else {

                            objToString(obj, lvl + 1, field1.getName());
                        }


                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    repeat = new char[lvl];
                    Arrays.fill(repeat, ' ');
                    //System.out.print(new String(repeat)+field.getName()+':');
                    fileWriter.write(new String(repeat) + ";" + field.getName() + ";\n");


                }

//
//            //System.out.println(field);
//            try {
//                System.out.println(field.getName() + ":" + field.get(object).toString());
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }

                lvl--;
                repeat = new char[lvl];
                Arrays.fill(repeat, ' ');
                //System.out.print(new String(repeat)+field.getName()+':');
                fileWriter.write(new String(repeat) + '{' + field.getType().getName() + '}' + '\n');

            }

        return null;
    }


    ArrayList<MySmartTextLine> mySmartTextLines;

    @Override
    public Object deSerialize(String pathname) throws IOException, ClassNotFoundException {

        try {

            fileReader = new FileReader(pathname);
            scanner = new Scanner(fileReader);
            mySmartTextLines = getSmartLines(scanner);

            //getClose(mySmartTextLines.get(31));
           // getCloseList(1);



            return linesToObj(mySmartTextLines,0);


        } finally {
            scanner.close();
            fileReader.close();
        }


       // return null;
    }


    private String deSerFile;

    private ArrayList<MySmartTextLine> getSmartLines(Scanner sc) {
        deSerFile="";
        ArrayList<MySmartTextLine> arrayList = new ArrayList<>();
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            deSerFile+=line+'\n';
            //arrayList.add(new MySmartTextLine(line));
            MySmartTextLine smartLine = new MySmartTextLine(line, line, 0);
            int i = 0;
            for (i = 0; ((line.charAt(i) == ' ') && (i < line.length())); i++) {
                smartLine.spaces++;
            }


            if ((smartLine.spaces == 0) && (smartLine.inLine == "null")) {
                smartLine.type = MySmartTextLine.Type.NullVariable;
            }

            if (line.charAt(i) == '{') {
                smartLine.type = MySmartTextLine.Type.Class;
                smartLine.inLine = line.substring(i + 1, line.lastIndexOf('}') );
            }
            if (line.charAt(i) == ':') {
                smartLine.type = MySmartTextLine.Type.Variable;
                smartLine.varClass = line.substring(i + 1, line.lastIndexOf(':') );
                smartLine.inLine = line.substring(line.lastIndexOf(':') + 1);
            }
            if (line.charAt(i) == ';') {
                smartLine.type = MySmartTextLine.Type.NotPrimVariable;
                smartLine.varClass = line.substring(i + 1, line.lastIndexOf(';') );
            }
            if (line.charAt(i) == '(') {
                smartLine.type = MySmartTextLine.Type.ID;
                smartLine.varClass = line.substring(i + 1, line.lastIndexOf(')') );
            }
            if (line.charAt(i) == '<') {
                smartLine.type = MySmartTextLine.Type.col;
                smartLine.varClass = line.substring(i + 1, line.lastIndexOf('>') );
            }

            arrayList.add(smartLine);
        }

        return arrayList;
    }



    private Object linesToObj(ArrayList<MySmartTextLine> scope, int depth) {

//        Object obj = recursiveCreate()

//        return obj;
        Object ret =null;
        try {

            ret =   recursiveCreate(Class.forName(scope.get(0).inLine));

//            for (Field field : ret.getClass().getFields())
//            {
//                field.setAccessible(true);
//            //    field.set(ret,linesToObj());
//                //TODO::something
//            }

            recursiveInit(ret,depth+1,getClose(scope.get(0)));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private  ArrayList<ArrayList<MySmartTextLine>> getCloseList(int depth)
    {

        return getCloseList(depth,mySmartTextLines);
       // return ret;
    }

    private  ArrayList<ArrayList<MySmartTextLine>> getCloseList(int depth,ArrayList<MySmartTextLine> bigList)
    {
        ArrayList<ArrayList<MySmartTextLine>>  ret = new ArrayList<ArrayList<MySmartTextLine>>();
        int skips=0;
        for (MySmartTextLine mySmartTextLine:bigList) {
            ArrayList<MySmartTextLine> temp = new ArrayList<MySmartTextLine>();
            if(skips>0){skips--;}
            else
            {

                if (mySmartTextLine.spaces==depth)
                {
                    temp=getClose(mySmartTextLine);
                    skips=temp.size()+1;
                    ret.add(temp);
                }
            }

        }

        return ret;
    }

    private ArrayList<MySmartTextLine> getClose(MySmartTextLine line1,ArrayList<MySmartTextLine> scope)
    {
        ArrayList<MySmartTextLine> ret = new ArrayList<MySmartTextLine>();

        for (MySmartTextLine mySmartTextLine:scope) {

            if ((mySmartTextLine.no>line1.no)&&(line1.spaces==mySmartTextLine.spaces)&&(line1.inLine.equals(mySmartTextLine.inLine)))
            {
                break;
            }
            if(mySmartTextLine.no>line1.no)  ret.add(mySmartTextLine);
        }

        return ret;
    }


    private ArrayList<MySmartTextLine> getClose(MySmartTextLine line1)
    {
        ArrayList<MySmartTextLine> ret = new ArrayList<MySmartTextLine>();

        for (MySmartTextLine mySmartTextLine:mySmartTextLines) {

            if ((mySmartTextLine.no>line1.no)&&(line1.spaces==mySmartTextLine.spaces)&&(line1.inLine.equals(mySmartTextLine.inLine)))
            {
            break;
            }
            if(mySmartTextLine.no>line1.no)  ret.add(mySmartTextLine);
        }

        return ret;
    }

    private void recursiveInit(Object object, int depth,ArrayList<MySmartTextLine> scope)
    {
        if(object instanceof Collection<?>)
    {
        ArrayList<MySmartTextLine> scope2 = getClose(scope.get(0));

        ArrayList<MySmartTextLine> headers =   getHeaders(scope,depth+1);

//        getCloseList(depth,scope).forEach(mySmartTextLines1 ->
//        {
//            System.out.println();
//            //linesToObj(mySmartTextLines1,depth+1);
//        });


        ArrayList<ArrayList<MySmartTextLine>> ll=getCloseList(depth+1,scope2);


        int i=0;
        for (ArrayList<MySmartTextLine> mySmartTextLines1 : getCloseList(depth + 1, getClose(scope.get(0)))) {
            ArrayList<MySmartTextLine> mySmartTextLines2=mySmartTextLines1;
            mySmartTextLines2.add(0,headers.get(i));
            mySmartTextLines2.add(headers.get(i));
            i+=2;
            ((Collection) object).add(linesToObj(mySmartTextLines2,depth+1));
        }

        // recursiveCreate(Class.forName(arrayList.get(0).inLine),depth+1);



    }else
        {
            int ii=0;

            ArrayList<MySmartTextLine> mslslist1 = getHeaders(scope,depth);
            ArrayList<ArrayList<MySmartTextLine>> mslslist2 = getCloseList(depth,scope);
            ArrayList<MySmartTextLine> mslslist3 = null;
        for (Field field : getAllFields(object))
        {
            field.setAccessible(true);
            //    field.set(ret,linesToObj());
            //TODO::i've rly forgot)



            int j=0;
      for(; 0!=mslslist2.get(j).get(0).varClass.compareTo(field.getName()); j++)
      {

      }



                Object tobj=null;
                    try {
                        if(isWrapperType(field.getType())) {
                            tobj = stringToType(mslslist2.get(j).get(0).inLine, field.getType());
                            ii++;
                        }
                        else
                            {
                                mslslist3=getClose(mslslist2.get(j).get(0),mslslist2.get(j));
                                MySmartTextLine l = mslslist1.get(j*2);
                                l.spaces++;
                                mslslist3.add(0,l);
                                mslslist3.add(l);
                                System.out.println(123);
                                tobj=linesToObj(mslslist3,depth+1);



                            }
                        field.set(object,tobj);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }



        }
        }
    }

    private Object recursiveCreate(Class<?> clazz)
    {
        Object generated = null;

        if(isWrapperType(clazz))
        return stringToType("0",clazz);


        if (clazz.isInterface())
    {

            return null;


    }else

        //if(clazz.getConstructors().length<2)
        if(false)
        {
            try {
                generated = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        else
            {
//                Constructor constructor = clazz.getConstructors()[0];



                Constructor<?>[] constructors= clazz.getConstructors();
                Arrays.sort(constructors,new ConstructorsComparator());

                Constructor constructor =constructors[0];

                Object[] zeroes = new Object[constructor.getParameterCount()];
                Class[] types = constructor.getParameterTypes();

                for(int i=0;i<types.length;i++)
                {
                    zeroes[i]=recursiveCreate(types[i]);
                }
                try {
                    generated = constructor.newInstance(zeroes);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }



        return generated;
    }

    private ArrayList<MySmartTextLine> getHeaders(ArrayList<MySmartTextLine> scope,int depth)
    {
        ArrayList<MySmartTextLine> ret = new ArrayList<MySmartTextLine>();

        for (MySmartTextLine mySmartTextLine:scope
             ) {
            if (mySmartTextLine.spaces==depth){ret.add(mySmartTextLine);}
        }

        return ret;
    }

    private class ConstructorsComparator implements Comparator<Constructor<?>>
    {

        @Override
        public int compare(Constructor<?> o1, Constructor<?> o2) {

        int c1;
        c1=o1.getParameterCount();
        int c2;
            c2=o2.getParameterCount();
            return Integer.compare(c1,c2);
        }
    }

    public String primObjToString(Object obj) {
//StringBuilder sb = new StringBuilder();
//
//Type type = obj.getClass();
//if(type==String.class)
//{
//    sb.append(obj);
//}else if(type==)
//
//
//        return sb.toString();


        if (obj.getClass().isArray()) {

            return "";
        } else {

            return String.valueOf(obj);
        }
    }

    private static class MySmartTextLine {
        public int no;
        public static int count=0;
        public String line;
        public String inLine;
        public int spaces;


        public String varClass = "";
        Type type = Type.Variable;
        public MySmartTextLine(String line, String inLine, int spaces) {
            this.line = line;
            this.inLine = inLine;
            this.spaces = spaces;
            no=count++;
        }



        public MySmartTextLine(String inLine, int spaces) {
            this.inLine = inLine;
            this.spaces = spaces;
        }

        public enum Type {
            Variable,
            NotPrimVariable,
            NullVariable,
            Class,
            ID,
            col,
        }
    }

}
