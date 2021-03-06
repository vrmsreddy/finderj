package br.com.t1tecnologia.finderj.enums;

/**
 *
 * @author alexandre
 */
public enum EstadoCivilEnum {

    SOLTEIRO("Solteiro (a)"),
    CASADO("Casado (a)"),
    DIVORCIADO("Divorciado (a)"),
    VIUVO("Viúvo (a)"),
    UNIAO_ESTAVEL("União Estável");

    private final String descricao;

    public String getDescricao() {
        return descricao;
    }

    private EstadoCivilEnum(String descricao) {
        this.descricao = descricao;
    }
}
