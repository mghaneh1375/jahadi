package four.group.jahadi.Tests;

import four.group.jahadi.Models.Drug;
import four.group.jahadi.Repository.DrugRepository;
import four.group.jahadi.Repository.ModuleRepository;
import four.group.jahadi.Tests.Modules.ModuleSeeder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Seeder {
    private final ModuleRepository moduleRepository;
    private final DrugRepository drugRepository;

    public void addCodeToDrugs() {
        Random random = new Random();
        List<Drug> drugs = drugRepository.findAllByCodeNotExist()
                .stream()
                .peek(drug -> drug.setCode((1000000 + Math.abs(random.nextInt() % 10000000)) + ""))
                .collect(Collectors.toList());

        if(drugs.size() > 0)
            drugRepository.saveAll(drugs);
    }

    public void moduleSeeder() {
        ModuleSeeder.seed(moduleRepository);
    }

}
