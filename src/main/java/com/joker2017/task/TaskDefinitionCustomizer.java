package com.joker2017.task;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;

import java.util.Collection;
import java.util.stream.Stream;

public class TaskDefinitionCustomizer extends CompilationCustomizer implements ExpressionTransformer {

    public TaskDefinitionCustomizer() {
        super(CompilePhase.CANONICALIZATION);
    }

    @Override
    public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
        source.getAST().getUnit().getModules().stream()
                .map(ModuleNode::getStatementBlock)
                .map(BlockStatement::getStatements)
                .flatMap(Collection::stream)
                .filter(it -> it instanceof ExpressionStatement)
                .map(it -> (ExpressionStatement)it)
                .map(ExpressionStatement::getExpression)
                .filter(it -> it instanceof MethodCallExpression)
                .map(it -> (MethodCallExpression)it)
                .filter(method -> Stream.of(method)
                            .map(MethodCallExpression::getMethod)
                            .filter(it -> it instanceof ConstantExpression)
                            .map(it -> (ConstantExpression)it)
                            .map(ConstantExpression::getValue)
                        .anyMatch("task"::equals))
                .forEach(this::transform);
    }

    @Override
    public Expression transform(Expression expression) {
        MethodCallExpression nameExp = (MethodCallExpression) ((TupleExpression) ((MethodCallExpression) expression)
                .getArguments())
                .getExpressions().get(0);

        ConstantExpression name = (ConstantExpression) nameExp.getMethod();

        ClosureExpression closureExpression = (ClosureExpression) ((TupleExpression) nameExp
                .getArguments())
                .getExpressions().get(0);

        ((MethodCallExpression) expression).setArguments(
                new TupleExpression(name,
                        closureExpression)
        );
        return expression;
    }
}
