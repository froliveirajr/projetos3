create table if not exists public.empresas (
    id bigserial primary key,
    cnpj varchar(18) not null unique,
    nome varchar(255) not null,
    senha varchar(255) not null,
    colaboradores integer not null,
    numero_beneficios integer not null,
    multibeneficio boolean not null,
    vida_util_cartao_anos double precision not null,
    taxa_turnover double precision not null,
    taxa_reemissao double precision not null,
    transacoes_mensais integer not null,
    porcentagem_digital_atual double precision not null
);

create index if not exists idx_empresas_nome on public.empresas (nome);
