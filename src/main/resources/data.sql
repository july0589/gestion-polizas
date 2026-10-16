
INSERT INTO poliza
    (tipo, estado, canon_mensual, meses_vigencia, ipc, prima)
VALUES
    ('INDIVIDUAL', 'ACTIVA', 1000000, 12, 0.05, 12000000);

INSERT INTO poliza
    (tipo, estado, canon_mensual, meses_vigencia, ipc, prima)
VALUES
    ('COLECTIVA', 'ACTIVA', 1500000, 12, 0.05, 18000000);

INSERT INTO riesgo (descripcion, estado, poliza_id)
SELECT 'Apartamento 101', 'ACTIVO', id
FROM poliza WHERE tipo = 'INDIVIDUAL';

INSERT INTO riesgo (descripcion, estado, poliza_id)
SELECT 'Apartamento 201', 'ACTIVO', id
FROM poliza WHERE tipo = 'COLECTIVA';

INSERT INTO riesgo (descripcion, estado, poliza_id)
SELECT 'Apartamento 202', 'ACTIVO', id
FROM poliza WHERE tipo = 'COLECTIVA';