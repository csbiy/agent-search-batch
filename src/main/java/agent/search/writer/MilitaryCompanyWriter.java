package agent.search.writer;

import agent.search.entity.MilitaryCompany;
import agent.search.repository.MilitaryCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MilitaryCompanyWriter implements ItemWriter<MilitaryCompany> {

    private final MilitaryCompanyRepository repository;

    @Override
    public void write(Chunk<? extends MilitaryCompany> chunk) {
        List<? extends MilitaryCompany> items = chunk.getItems();
        items.forEach(this::handleItem);
    }

    private void handleItem(MilitaryCompany item) {
        repository.findByName(item.getName())
                .ifPresentOrElse(
                        (foundItem) -> itemExistCaseHandler(foundItem, item),
                        () -> newItemHandler(item)
                );
    }

    private void itemExistCaseHandler(MilitaryCompany foundItem, MilitaryCompany newItem) {
        foundItem.update(newItem);
    }

    private void newItemHandler(MilitaryCompany newItem) {
        repository.save(newItem);
    }
}
