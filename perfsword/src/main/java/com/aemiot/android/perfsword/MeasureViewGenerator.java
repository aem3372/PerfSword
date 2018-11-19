package com.aemiot.android.perfsword;

import android.content.Context;
import android.graphics.Canvas;
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

public class MeasureViewGenerator {

    private static ThreadLocal<Map<String, Class>> cacheMap = new ThreadLocal<Map<String, Class>>() {
        @Override
        protected Map<String, Class> initialValue() {
            return new HashMap<>();
        }
    };

    private static ThreadLocal<Map<Class, Constructor>> cacheConstructor = new ThreadLocal<Map<Class, Constructor>>() {
        @Override
        protected Map<Class, Constructor> initialValue() {
            return new HashMap<>();
        }
    };

    public static View generatorObject(Class supperClz, Context context, AttributeSet attributeSet) {
        Constructor constructor = cacheConstructor.get().get(supperClz);
        if (constructor == null) {
            try {
                Class clz = generator(supperClz);
                constructor = clz.getConstructor(Context.class, AttributeSet.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            cacheConstructor.get().put(supperClz, constructor);
        }
        try {
            return (View) constructor.newInstance(context, attributeSet);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
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
         *     Monitor.meaureStart(this);
         *     super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         *     Monitor.measureEnd(this);
         * }
         */
        method = generatedType.getMethod(TypeId.VOID, "onMeasure", TypeId.INT, TypeId.INT);
        Code code = dexMaker.declare(method, Modifier.PROTECTED);
        MethodId monitorStartMethod = log.getMethod(TypeId.VOID, "measureStart", TypeId.OBJECT);
        code.invokeStatic(monitorStartMethod, null, code.getThis(generatedType));
        MethodId supperMethod = supperType.getMethod(TypeId.VOID, "onMeasure", TypeId.INT, TypeId.INT);
        code.invokeSuper(supperMethod, null, localThis, code.getParameter(0, TypeId.INT), code.getParameter(1, TypeId.INT));
        MethodId monitorEndMethod = log.getMethod(TypeId.VOID, "measureEnd", TypeId.OBJECT);
        code.invokeStatic(monitorEndMethod, null, code.getThis(generatedType));
        code.returnVoid();

        /**
         * @Override
         * protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
         *     Monitor.layoutStart(this);
         *     super.onMeasure(changed, left, top, right, bottom);
         *     Monitor.layoutEnd(this);
         * }
         */
        method = generatedType.getMethod(TypeId.VOID, "onLayout", TypeId.BOOLEAN, TypeId.INT, TypeId.INT, TypeId.INT, TypeId.INT);
        code = dexMaker.declare(method, Modifier.PROTECTED);
        MethodId layoutStartMethod = log.getMethod(TypeId.VOID, "layoutStart", TypeId.OBJECT);
        code.invokeStatic(layoutStartMethod, null, code.getThis(generatedType));
        supperMethod = supperType.getMethod(TypeId.VOID, "onLayout", TypeId.BOOLEAN, TypeId.INT, TypeId.INT, TypeId.INT, TypeId.INT);
        code.invokeSuper(supperMethod, null, localThis, code.getParameter(0, TypeId.BOOLEAN),
                code.getParameter(1, TypeId.INT), code.getParameter(2, TypeId.INT),
                code.getParameter(3, TypeId.INT), code.getParameter(4, TypeId.INT));
        MethodId layoutEndMethod = log.getMethod(TypeId.VOID, "layoutEnd", TypeId.OBJECT);
        code.invokeStatic(layoutEndMethod, null, code.getThis(generatedType));
        code.returnVoid();

        /**
         * @Override
         * protected void onDraw(Canvas canvas) {
         *     Monitor.drawStart(this);
         *     super.onDraw(canvas);
         *     Monitor.drawEnd(this);
         * }
         */
        TypeId<Canvas> canvasType = TypeId.get(Canvas.class);
        method = generatedType.getMethod(TypeId.VOID, "onDraw", canvasType);
        code = dexMaker.declare(method, Modifier.PROTECTED);
        MethodId drawStartMethod = log.getMethod(TypeId.VOID, "drawStart", TypeId.OBJECT);
        code.invokeStatic(drawStartMethod, null, code.getThis(generatedType));
        supperMethod = supperType.getMethod(TypeId.VOID, "onDraw", canvasType);
        code.invokeSuper(supperMethod, null, localThis, code.getParameter(0, canvasType));
        MethodId drawEndMethod = log.getMethod(TypeId.VOID, "drawEnd", TypeId.OBJECT);
        code.invokeStatic(drawEndMethod, null, code.getThis(generatedType));
        code.returnVoid();
    }
}
