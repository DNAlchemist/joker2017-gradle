package ru.jpoint2017.task;

import groovy.lang.GroovyRuntimeException;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ExpressionTransformer;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
                .filter(method -> Optional.of(method)
                            .map(MethodCallExpression::getMethod)
                            .filter(it -> it instanceof ConstantExpression)
                            .map(it -> (ConstantExpression)it)
                            .map(ConstantExpression::getValue)
                            .filter("task"::equals)
                            .isPresent())
                .forEach(this::transform);
    }

    @Override
    public Expression transform(Expression expression) {
        MethodCallExpression createTaskExpression = (MethodCallExpression)expression;
        TupleExpression tupleExpression = (TupleExpression)createTaskExpression.getArguments();
        List<Expression> arguments = tupleExpression.getExpressions();
        if(arguments.size() != 1 || !(arguments.get(0) instanceof MethodCallExpression)) {
            return expression;
        }
        MethodCallExpression methodCallExpression = (MethodCallExpression)arguments.get(0);
        ConstantExpression taskName = Optional.of(methodCallExpression)
                .map(MethodCallExpression::getMethod)
                .map(it -> (ConstantExpression)it)
                .orElseThrow(GroovyRuntimeException::new);


        List<Expression> args = ((TupleExpression)methodCallExpression.getArguments()).getExpressions();
        if(args.size() != 1 || !(args.get(0) instanceof ClosureExpression)) {
            throw new IllegalArgumentException("Ошибка компиляции: в параметрах должен быть только кложур");
        }
        ClosureExpression closureExpression = (ClosureExpression)args.get(0);
        createTaskExpression.setArguments(new TupleExpression(taskName, closureExpression));
        return expression;
    }
}
