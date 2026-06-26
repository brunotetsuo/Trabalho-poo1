-- =============================================
-- Generators e Triggers para auto-incremento
-- Firebird - tabelas: MEMBRO, ACERVO, EMPRESTIMO, RESERVA
-- =============================================

-- =====================
-- MEMBRO
-- =====================
CREATE GENERATOR GEN_MEMBRO_ID;

SET TERM !! ;
CREATE TRIGGER TRG_MEMBRO_ID FOR MEMBRO ACTIVE
BEFORE INSERT POSITION 0
AS BEGIN
    IF (NEW.ID IS NULL) THEN
        NEW.ID = GEN_ID(GEN_MEMBRO_ID, 1);
END !!
SET TERM ; !!

-- =====================
-- ACERVO
-- =====================
CREATE GENERATOR GEN_ACERVO_ID;

SET TERM !! ;
CREATE TRIGGER TRG_ACERVO_ID FOR ACERVO ACTIVE
BEFORE INSERT POSITION 0
AS BEGIN
    IF (NEW.ID IS NULL) THEN
        NEW.ID = GEN_ID(GEN_ACERVO_ID, 1);
END !!
SET TERM ; !!

-- =====================
-- EMPRESTIMO
-- =====================
CREATE GENERATOR GEN_EMPRESTIMO_ID;

SET TERM !! ;
CREATE TRIGGER TRG_EMPRESTIMO_ID FOR EMPRESTIMO ACTIVE
BEFORE INSERT POSITION 0
AS BEGIN
    IF (NEW.ID IS NULL) THEN
        NEW.ID = GEN_ID(GEN_EMPRESTIMO_ID, 1);
END !!
SET TERM ; !!

-- =====================
-- RESERVA
-- =====================
CREATE GENERATOR GEN_RESERVA_ID;

SET TERM !! ;
CREATE TRIGGER TRG_RESERVA_ID FOR RESERVA ACTIVE
BEFORE INSERT POSITION 0
AS BEGIN
    IF (NEW.ID IS NULL) THEN
        NEW.ID = GEN_ID(GEN_RESERVA_ID, 1);
END !!
SET TERM ; !!
