package com.ahnis.servaia.journal.embedding;

import com.ahnis.servaia.journal.entity.Journal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JournalEmbeddingService {
    private final VectorStore vectorStore;

    @Async
    public void saveJournalEmbeddings(Journal journal) {

        try {
            var document = new Document(
                    journal.getId(),
                    journal.getContent(),
                    Map.of(
                            "title", journal.getTitle(),
                            "userId", journal.getUserId(),
                            "createdAt", journal.getCreatedAt(),
                            "modifiedAt", journal.getModifiedAt()

                    )
            );
            var textSplitter = new TokenTextSplitter();
            var splitDocuments = textSplitter.apply(List.of(document));
            log.info("Adding document {} ", document);
            vectorStore.add(splitDocuments);

            log.info("Added document {} ", document);

        } catch (Exception e) {
            log.error("Failed to save journal embedding {} error {} ", journal, e);
        }
    }

    @Async
    public void updateJournalEmbeddings(Journal journal) {
        try {
            deleteJournalEmbeddings(journal.getId());
            saveJournalEmbeddings(journal);
            log.info("Updated embeddings for journal {} ", journal);
        } catch (Exception e) {
            log.error("Failed to update journal embedding {} error {} ", journal, e);
        }
    }

    @Async
    public void deleteJournalEmbeddings(String journalId) {
        try {
            // Delete embeddings by userId and journalId
            vectorStore.delete(List.of(journalId)); // Assuming journalId is the document ID in the vector store
            log.info("Deleted embeddings for journalId {} ", journalId);
        } catch (Exception e) {
            log.error("Failed to delete journal embedding for journalId {} error {} ", journalId, e);
        }
    }

}

