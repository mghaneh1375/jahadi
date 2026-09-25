package four.group.jahadi.Tests.Modules.SubModules;

import four.group.jahadi.Models.Module;
import four.group.jahadi.Models.Question.*;
import four.group.jahadi.Models.SubModule;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static four.group.jahadi.Tests.Modules.ModuleSeeder.moduleRepository;

public class Helper {

    public static SubModule findSubModule(String moduleName, String subModuleName) {
        List<Module> modules = moduleRepository.findAll();
        AtomicReference<List<SubModule>> subModules = new AtomicReference<>(null);
        modules
                .stream()
                .filter(module -> module.getName().equals(moduleName))
                .findFirst()
                .ifPresent(module -> {
                    subModules.set(module.getSubModules());
                });

        return subModules.get() == null ? null
                : subModules.get().stream().map(SubModule::getName).anyMatch(s -> s.equals(subModuleName))
                ? subModules.get().stream().filter(subModule -> subModule.getName().equals(subModuleName)).findFirst().get()
                : null;
    }

    public static Question findQuestionInSubModule(SubModule subModule, Object wanted) {
        if(subModule == null) return null;

        Optional<Question> optionalQuestion = subModule.getQuestions().stream().filter(question -> {
            if(question instanceof CheckListGroupQuestion) {
                if(wanted instanceof List) return false;
                return Objects.equals(((CheckListGroupQuestion) question).getSectionTitle(), wanted.toString());
            }
            if(question instanceof GroupQuestion) {
                if(wanted instanceof List) return false;
                return Objects.equals(((GroupQuestion) question).getSectionTitle(), wanted.toString());
            }
            if(question instanceof SimpleQuestion) {
                if(wanted instanceof List) return false;
                return Objects.equals(((SimpleQuestion) question).getQuestion(), wanted.toString());
            }
            if(question instanceof TableQuestion) {
                if(wanted instanceof String) return false;
                return isSameArr(((TableQuestion)question).getHeaders(), (List<String>) wanted);
            }

            return false;
        }).findFirst();

        return optionalQuestion.orElse(null);
    }

    public static ObjectId findSimpleQuestionInList(CheckListGroupQuestion parentQuestion, String questionText) {
        if(parentQuestion == null) return new ObjectId();
        Optional<SimpleQuestion> simpleQuestion = parentQuestion.getQuestions()
                .stream()
                .filter(question -> question.getQuestion().equals(questionText))
                .findFirst();

        if(simpleQuestion.isPresent())
            return simpleQuestion.get().getId();

        return new ObjectId();
    }

    public static ObjectId findSimpleQuestionInList(GroupQuestion parentQuestion, String questionText) {
        if(parentQuestion == null) return new ObjectId();
        Optional<Question> simpleQuestion = parentQuestion.getQuestions()
                .stream()
                .filter(question -> question instanceof SimpleQuestion && ((SimpleQuestion)question).getQuestion().equals(questionText))
                .findFirst();

        if(simpleQuestion.isPresent())
            return simpleQuestion.get().getId();

        return new ObjectId();
    }

    public static ObjectId findTableQuestionInList(GroupQuestion parentQuestion) {
        if(parentQuestion == null) return new ObjectId();
        Optional<Question> tableQuestion = parentQuestion.getQuestions()
                .stream()
                .filter(question -> question instanceof TableQuestion)
                .findFirst();

        if(tableQuestion.isPresent())
            return tableQuestion.get().getId();

        return new ObjectId();
    }

    private static boolean isSameArr(List<String> l1, List<String> l2) {
        if(l1 == null && l2 != null) return false;
        if(l2 == null && l1 != null) return false;
        if(l1 == null) return true;

        if(l1.size() != l2.size()) return false;
        for(int i = 0; i < l1.size(); i++) {
            if(!Objects.equals(l1.get(i), l2.get(i)))
                return false;
        }

        return true;
    }
}
