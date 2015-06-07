package me.dwtj;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.sun.source.util.Trees;
import com.sun.source.util.TreePath;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;


/**
 * An annotation processor which provides an example for a simple manipulation of the javac AST.
 */
@SupportedAnnotationTypes("me.dwtj.Read")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ASTReader extends AbstractProcessor
{
    Trees tree_utils;

    @Override
    public void init(ProcessingEnvironment proc_env)
    {
        tree_utils = Trees.instance(proc_env);
        assert tree_utils != null;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment round_env)
    {
        List<CompilationUnitTree> units = extractCompilationUnitTrees(round_env);

        for (CompilationUnitTree unit : units) {
            System.out.println("~~~~~~");
            System.out.println(unit);
            System.out.println("~~~~~~");
        }
        return false;
    }
    
    private List<CompilationUnitTree> extractCompilationUnitTrees(RoundEnvironment round_env)
    {
        List<CompilationUnitTree> units = new ArrayList<CompilationUnitTree>();
        for (Element elem : round_env.getRootElements())
        {
            CompilationUnitTree unit = toCompilationUnitTree(elem);
            if (unit == null) {
                System.out.println("Unable to obtain compilation unit for element" + elem);
                continue;
            }

            if (!units.contains(unit)) {
                units.add(unit);
            }
        }

        return units;
    }

    /**
     * Returns the `CompilationUnitTree` in which the given element is defined.
     */
    private CompilationUnitTree toCompilationUnitTree(Element elem)
    {
        TreePath path = tree_utils.getPath(elem);
        return path == null ? null : path.getCompilationUnit();
    }
}
