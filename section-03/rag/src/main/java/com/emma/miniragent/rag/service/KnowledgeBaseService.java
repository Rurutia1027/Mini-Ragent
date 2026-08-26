package com.emma.miniragent.rag.service;

import com.emma.miniragent.framework.exception.ClientException;
import com.emma.miniragent.rag.dto.KnowledgeBaseRequest;
import com.emma.miniragent.rag.dto.KnowledgeBaseVO;
import com.emma.miniragent.rag.model.KnowledgeBase;
import com.emma.miniragent.rag.repository.KnowledgeBaseRepository;

import java.util.List;

public class KnowledgeBaseService {

    private final KnowledgeBaseRepository knowledgeBaseRepository;

    public KnowledgeBaseService(KnowledgeBaseRepository knowledgeBaseRepository) {
        this.knowledgeBaseRepository = knowledgeBaseRepository;
    }

    public List<KnowledgeBaseVO> list() {
        return knowledgeBaseRepository.findAll().stream().map(this::toVO).toList();
    }

    public KnowledgeBaseVO get(long id) {
        return toVO(knowledgeBaseRepository.requireById(id));
    }

    public KnowledgeBaseVO create(KnowledgeBaseRequest request) {
        validateName(request.name());
        return toVO(knowledgeBaseRepository.insert(request.name().trim(), trimOrNull(request.description())));
    }

    public KnowledgeBaseVO update(long id, KnowledgeBaseRequest request) {
        validateName(request.name());
        return toVO(knowledgeBaseRepository.update(id, request.name().trim(), trimOrNull(request.description())));
    }

    public void delete(long id) {
        knowledgeBaseRepository.delete(id);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ClientException("知识库名称不能为空");
        }
    }

    private static String trimOrNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private KnowledgeBaseVO toVO(KnowledgeBase kb) {
        return new KnowledgeBaseVO(kb.id(), kb.name(), kb.description(), kb.createdAt(), kb.updatedAt());
    }
}
