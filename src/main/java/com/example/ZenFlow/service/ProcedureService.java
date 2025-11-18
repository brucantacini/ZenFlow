package com.example.ZenFlow.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
public class ProcedureService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Chama a procedure PC_INSERIR_DEPARTAMENTO
     * @param nomeDepto Nome do departamento
     * @param descricao Descrição do departamento (opcional)
     * @return ID do departamento criado
     */
    public Long inserirDepartamento(String nomeDepto, String descricao) {
        StoredProcedureQuery procedure = entityManager
                .createStoredProcedureQuery("PC_INSERIR_DEPARTAMENTO")
                .registerStoredProcedureParameter("P_NOME_DEPTO", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("P_DESCRICAO", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("P_ID_DEPTO", Long.class, ParameterMode.OUT)
                .setParameter("P_NOME_DEPTO", nomeDepto)
                .setParameter("P_DESCRICAO", descricao);

        procedure.execute();

        return (Long) procedure.getOutputParameterValue("P_ID_DEPTO");
    }

    /**
     * Chama a procedure PC_INSERIR_NIVEL_ESTRESSE
     * @param nivel Nível de estresse (1-5)
     * @param descricao Descrição do nível
     * @return ID do nível de estresse criado
     */
    public Long inserirNivelEstresse(Integer nivel, String descricao) {
        StoredProcedureQuery procedure = entityManager
                .createStoredProcedureQuery("PC_INSERIR_NIVEL_ESTRESSE")
                .registerStoredProcedureParameter("P_NIVEL", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("P_DESCRICAO", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("P_ID_NIVEL", Long.class, ParameterMode.OUT)
                .setParameter("P_NIVEL", nivel)
                .setParameter("P_DESCRICAO", descricao);

        procedure.execute();

        return (Long) procedure.getOutputParameterValue("P_ID_NIVEL");
    }

    /**
     * Chama a procedure PC_INSERIR_AVALIACAO
     * @param idDepto ID do departamento
     * @param idNivelEstresse ID do nível de estresse
     * @param comentario Comentário (opcional)
     * @return ID da avaliação criada
     */
    public Long inserirAvaliacao(Long idDepto, Long idNivelEstresse, String comentario) {
        StoredProcedureQuery procedure = entityManager
                .createStoredProcedureQuery("PC_INSERIR_AVALIACAO")
                .registerStoredProcedureParameter("P_ID_DEPTO", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("P_ID_NIVEL_ESTRESSE", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("P_COMENTARIO", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("P_ID_AVALIACAO", Long.class, ParameterMode.OUT)
                .setParameter("P_ID_DEPTO", idDepto)
                .setParameter("P_ID_NIVEL_ESTRESSE", idNivelEstresse)
                .setParameter("P_COMENTARIO", comentario);

        procedure.execute();

        return (Long) procedure.getOutputParameterValue("P_ID_AVALIACAO");
    }

    /**
     * Chama a função FN_CALCULAR_MEDIA_SEMANAL
     * @param idDepto ID do departamento
     * @param dataInicio Data de início
     * @param dataFim Data de fim
     * @return Média de estresse calculada
     */
    public Double calcularMediaSemanal(Long idDepto, LocalDateTime dataInicio, LocalDateTime dataFim) {
        String sql = "SELECT FN_CALCULAR_MEDIA_SEMANAL(:idDepto, :dataInicio, :dataFim) FROM DUAL";
        
        Object result = entityManager.createNativeQuery(sql)
                .setParameter("idDepto", idDepto)
                .setParameter("dataInicio", convertToDate(dataInicio))
                .setParameter("dataFim", convertToDate(dataFim))
                .getSingleResult();
        
        if (result == null) {
            return 0.0;
        }
        return ((java.math.BigDecimal) result).doubleValue();
    }

    /**
     * Chama a função FN_VALIDAR_NIVEL_ESTRESSE
     * @param nivel Nível de estresse a validar
     * @return Mensagem de validação
     */
    public String validarNivelEstresse(Integer nivel) {
        String sql = "SELECT FN_VALIDAR_NIVEL_ESTRESSE(:nivel) FROM DUAL";
        
        Object result = entityManager.createNativeQuery(sql)
                .setParameter("nivel", nivel)
                .getSingleResult();
        
        return result != null ? result.toString() : "Erro ao validar nível";
    }

    /**
     * Chama a função FN_CONTAR_REGISTROS_PERIODO
     * @param idDepto ID do departamento
     * @param dataInicio Data de início
     * @param dataFim Data de fim
     * @return Total de registros no período
     */
    public Long contarRegistrosPeriodo(Long idDepto, LocalDateTime dataInicio, LocalDateTime dataFim) {
        String sql = "SELECT FN_CONTAR_REGISTROS_PERIODO(:idDepto, :dataInicio, :dataFim) FROM DUAL";
        
        Object result = entityManager.createNativeQuery(sql)
                .setParameter("idDepto", idDepto)
                .setParameter("dataInicio", convertToDate(dataInicio))
                .setParameter("dataFim", convertToDate(dataFim))
                .getSingleResult();
        
        if (result == null) {
            return 0L;
        }
        return ((java.math.BigDecimal) result).longValue();
    }

    /**
     * Converte LocalDateTime para Date (necessário para Oracle)
     */
    private Date convertToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}

