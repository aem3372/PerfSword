package com.aem.perfsword;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.android.dx.Code;
import com.android.dx.DexMaker;
import com.android.dx.Local;
import com.android.dx.MethodId;
import com.android.dx.TypeId;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class MeasureViewGenerator  {

    private static ThreadLocal<Map<String, Class>> cacheMap = new ThreadLocal<Map<String, Class>>() {
        @Override
        protected Map<String, Class> initialValue() {
            return new HashMap<String, Class>();
        }
    };

    public static View generatorObject(Class supperClz, Context context, AttributeSet attributeSet) {
        try {
            Class clz = generator(supperClz);
            Constructor constructor = clz.getConstructor(Context.class, AttributeSet.class);
            return (View) constructor.newInstance(context, attributeSet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> generator(Class supperClz) throws IOException, ClassNotFoundException {
        String name = supperClz.getName();
        if (null == cacheMap.get().get(name)) {
            DexMaker dexMaker = new DexMaker();
            TypeId supperType = TypeId.get(supperClz);
            // Generate a HelloWorld class.
            TypeId<?> generatedType = TypeId.get("L" + name.replace('.', '/') + "$MeasureWrapper" + ";");
            dexMaker.declare(generatedType, "MeasureView" + ".generated", Modifier.PUBLIC | Modifier.FINAL, supperType);
            generateHelloMethod(dexMaker, generatedType, supperType);

            // Create the dex file and load it.
            ClassLoader loader = dexMaker.generateAndLoad(
                    MeasureViewGenerator.class.getClassLoader(), null);
            Class<?> clz = loader.loadClass( name + "$MeasureWrapper");

            cacheMap.get().put(name, clz);
        }
        return cacheMap.get().get(name);
    }

    private static void generateHelloMethod(DexMaker dexMaker, TypeId<?> generatedType, TypeId<?> supperType) {
        TypeId<Context> contextType = TypeId.get(Context.class);
        TypeId<AttributeSet> attrType = TypeId.get(AttributeSet.class);
        TypeId<Monitor> log = TypeId.get(Monitor.class);

        /**
         * public View$MeasureWrapper(Context context) {
         *     super(context);
         * }
         */
        MethodId method = generatedType.getConstructor(contextType);
        Code constructorCode = dexMaker.declare(method, Modifier.PUBLIC);
        Local<?> localThis = constructorCode.getThis(generatedType);
        Local<Context> localContext = constructorCode.getParameter(0, contextType);
        MethodId superConstructor = supperType.getConstructor(contextType);
        constructorCode.invokeDirect(superConstructor, null, localThis, localContext);
        constructorCode.returnVoid();

        /**
         * public View$MeasureWrapper(Context context, AttributeSet attrs) {
         *     super(context, attrs);
         * }
         */
        method = generatedType.getConstructor(contextType, attrType);
        constructorCode = dexMaker.declare(method, Modifier.PUBLIC);
        localThis = constructorCode.getThis(generatedType);
        localContext = constructorCode.getParameter(0, contextType);
        Local<AttributeSet> localAttr = constructorCode.getParameter(1, attrType);
        superConstructor = supperType.getConstructor(contextType, attrType);
        constructorCode.invokeDirect(superConstructor, null, localThis, localContext, localAttr);
        constructorCode.returnVoid();

        /**
         * public MeasureViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
         *     super(context, attrs, defStyleAttr);
         * }
         */
        method = generatedType.getConstructor(contextType, attrType, TypeId.INT);
        constructorCode = dexMaker.declare(method, Modifier.PUBLIC);
        localThis = constructorCode.getThis(generatedType);
        localContext = constructorCode.getParameter(0, contextType);
        localAttr = constructorCode.getParameter(1, attrType);
        Local<Integer> localDefStyleAttr = constructorCode.getParameter(2, TypeId.INT);
        superConstructor = supperType.getConstructor(contextType, attrType, TypeId.INT);
        constructorCode.invokeDirect(superConstructor, null, localThis, localContext, localAttr, localDefStyleAttr);
        constructorCode.returnVoid();

        /**
         * @Override
         * protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         *     super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         * }
         */
        method = generatedType.getMethod(TypeId.VOID, "onMeasure", TypeId.INT, TypeId.INT);
        Code code = dexMaker.declare(method, Modifier.PROTECTED);
        Local<Integer> localWidthMeasureSpec = code.getParameter(0, TypeId.INT);
        Local<Integer> localHeightMeasureSpec = code.getParameter(1, TypeId.INT);
        MethodId logMethod = log.getMethod(TypeId.VOID, "monitor");
        code.invokeStatic(logMethod, null);
        MethodId supperMethod = supperType.getMethod(TypeId.VOID, "onMeasure", TypeId.INT, TypeId.INT);
        code.invokeSuper(supperMethod, null, localThis, localWidthMeasureSpec, localHeightMeasureSpec);
        code.returnVoid();

    }
}
