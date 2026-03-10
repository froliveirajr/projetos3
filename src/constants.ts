import { Task, ColumnId, TaskCategory, TeamMember } from './types';

export const INITIAL_TASKS: Task[] = [
  // SEMANA 3: ONBOARDING (23/02 - 28/02)
  {
    id: 'w3-1',
    title: 'Definição dos Grupos',
    startDate: '2026-02-23',
    dueDate: '2026-02-28',
    assignedTo: ['1', '2'],
    columnId: ColumnId.DONE,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },
  {
    id: 'w3-2',
    title: 'Organização dos Atendimentos',
    startDate: '2026-02-23',
    dueDate: '2026-02-28',
    assignedTo: ['1'],
    columnId: ColumnId.DONE,
    category: TaskCategory.PROCESSO,
    priority: 'Baixa'
  },
  {
    id: 'w3-3',
    title: 'Apresentação dos Problemas',
    startDate: '2026-02-23',
    dueDate: '2026-02-28',
    assignedTo: ['1', '2', '3', '4', '5'],
    columnId: ColumnId.DONE,
    category: TaskCategory.KICKOFF,
    priority: 'Alta'
  },

  // SEMANA 4: FERIADO (02/03 - 07/03) - DATA MAGNA
  {
    id: 'w4-1',
    title: 'Feriado - Data Magna (Sem Entrega)',
    startDate: '2026-03-02',
    dueDate: '2026-03-07',
    assignedTo: [],
    columnId: ColumnId.DONE,
    category: TaskCategory.PROCESSO,
    priority: 'Baixa'
  },

  // SEMANA 5: KICKOFF (09/03 - 14/03) - SEMANA ATUAL
  {
    id: 'w5-1',
    title: 'Project Model Canvas',
    startDate: '2026-03-09',
    dueDate: '2026-03-14',
    assignedTo: ['1', '2'],
    columnId: ColumnId.DOING,
    category: TaskCategory.KICKOFF,
    priority: 'Alta'
  },
  {
    id: 'w5-2',
    title: 'Descrição da Problemática',
    startDate: '2026-03-09',
    dueDate: '2026-03-14',
    assignedTo: ['2', '3'],
    columnId: ColumnId.DOING,
    category: TaskCategory.KICKOFF,
    priority: 'Média'
  },
  {
    id: 'w5-3',
    title: 'Análise de Concorrentes',
    startDate: '2026-03-09',
    dueDate: '2026-03-14',
    assignedTo: ['3'],
    columnId: ColumnId.DOING,
    category: TaskCategory.KICKOFF,
    priority: 'Média'
  },
  {
    id: 'w5-4',
    title: 'Cronograma Macro / Roadmap',
    startDate: '2026-03-09',
    dueDate: '2026-03-14',
    assignedTo: ['1'],
    columnId: ColumnId.DOING,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },
  {
    id: 'w5-5',
    title: 'Fluxo de Versionamento (GitHub)',
    startDate: '2026-03-09',
    dueDate: '2026-03-14',
    assignedTo: ['3', '4'],
    columnId: ColumnId.DOING,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w5-6',
    title: 'Google Sites + README',
    startDate: '2026-03-09',
    dueDate: '2026-03-14',
    assignedTo: ['4', '5'],
    columnId: ColumnId.DOING,
    category: TaskCategory.PROCESSO,
    priority: 'Baixa'
  },
  {
    id: 'w5-7',
    title: 'Entrega: Kickoff + Apresentação',
    description: 'Evento de entrega que reúne as atividades da semana.',
    startDate: '2026-03-14',
    dueDate: '2026-03-14',
    assignedTo: ['1', '2', '3', '4', '5'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.KICKOFF,
    priority: 'Alta',
    checklist: [
      { id: 'c51', text: 'Slides prontos', completed: false },
      { id: 'c52', text: 'PMC validado', completed: false },
      { id: 'c53', text: 'Repositório configurado', completed: false }
    ]
  },

  // SEMANA 6: APROFUNDAMENTO (16/03 - 21/03)
  {
    id: 'w6-1',
    title: 'Personas',
    startDate: '2026-03-16',
    dueDate: '2026-03-21',
    assignedTo: ['2'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.KICKOFF,
    priority: 'Média'
  },
  {
    id: 'w6-2',
    title: 'User Story Mapping',
    startDate: '2026-03-16',
    dueDate: '2026-03-21',
    assignedTo: ['1', '2'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Alta'
  },
  {
    id: 'w6-3',
    title: 'Detalhamento das Histórias',
    startDate: '2026-03-16',
    dueDate: '2026-03-21',
    assignedTo: ['3', '4', '5'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },
  {
    id: 'w6-4',
    title: 'Protótipo de Baixa Fidelidade',
    startDate: '2026-03-16',
    dueDate: '2026-03-21',
    assignedTo: ['4'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w6-5',
    title: 'Screencast do Protótipo',
    startDate: '2026-03-16',
    dueDate: '2026-03-21',
    assignedTo: ['5'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },
  {
    id: 'w6-6',
    title: 'Entrega: Aprofundamento',
    startDate: '2026-03-21',
    dueDate: '2026-03-21',
    assignedTo: ['1', '2', '3', '4', '5'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Alta'
  },

  // SEMANA 7: IMPLEMENTAÇÃO (23/03 - 28/03)
  {
    id: 'w7-1',
    title: 'Validação das Histórias de Usuário',
    startDate: '2026-03-23',
    dueDate: '2026-03-28',
    assignedTo: ['1', '2', '3'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },

  // SEMANA 8: IMPRENSADO (30/03 - 04/04)
  {
    id: 'w8-1',
    title: 'Semana de Imprensado',
    startDate: '2026-03-30',
    dueDate: '2026-04-04',
    assignedTo: [],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Baixa'
  },

  // SEMANA 9: SR1 (06/04 - 11/04)
  {
    id: 'w9-1',
    title: 'Detalhamento Pontos Fortes e Melhorias',
    startDate: '2026-04-06',
    dueDate: '2026-04-11',
    assignedTo: ['1', '2'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },
  {
    id: 'w9-2',
    title: 'Análise SWOT',
    startDate: '2026-04-06',
    dueDate: '2026-04-11',
    assignedTo: ['1', '2'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.PROCESSO,
    priority: 'Baixa'
  },
  {
    id: 'w9-3',
    title: 'Aplicação Rodando + Screencast',
    startDate: '2026-04-06',
    dueDate: '2026-04-11',
    assignedTo: ['3', '4'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w9-4',
    title: 'Entrega Oficial SR1',
    startDate: '2026-04-11',
    dueDate: '2026-04-11',
    assignedTo: ['1', '2', '3', '4', '5'],
    columnId: ColumnId.BACKLOG_SR1,
    category: TaskCategory.SR1,
    priority: 'Alta'
  },

  // SEMANA 10: FEEDBACK SR1 (13/04 - 18/04)
  {
    id: 'w10-1',
    title: 'Ajustar Planejamento (Feedback SR1)',
    startDate: '2026-04-13',
    dueDate: '2026-04-18',
    assignedTo: ['1', '2'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  },

  // SEMANA 11-16: ACOMPANHAMENTO - IMPLEMENTAÇÃO
  {
    id: 'w11-1',
    title: 'Implementação: Ciclo 1',
    startDate: '2026-04-20',
    dueDate: '2026-04-25',
    assignedTo: ['3', '4'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w12-1',
    title: 'Implementação: Ciclo 2',
    startDate: '2026-04-27',
    dueDate: '2026-05-02',
    assignedTo: ['3', '4'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w13-1',
    title: 'Implementação: Ciclo 3',
    startDate: '2026-05-04',
    dueDate: '2026-05-09',
    assignedTo: ['3', '4'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w14-1',
    title: 'Implementação: Ciclo 4',
    startDate: '2026-05-11',
    dueDate: '2026-05-16',
    assignedTo: ['3', '4'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w15-1',
    title: 'Implementação: Ciclo 5',
    startDate: '2026-05-18',
    dueDate: '2026-05-23',
    assignedTo: ['3', '4'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w16-1',
    title: 'Implementação: Ciclo 6',
    startDate: '2026-05-25',
    dueDate: '2026-05-30',
    assignedTo: ['3', '4'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },

  // SEMANA 17: PREPARAÇÃO SR2 (01/06 - 06/06)
  {
    id: 'w17-1',
    title: 'Restante das Histórias Implementadas',
    startDate: '2026-06-01',
    dueDate: '2026-06-06',
    assignedTo: ['3', '4'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w17-2',
    title: 'Ambiente e Versionamento Funcionando',
    startDate: '2026-06-01',
    dueDate: '2026-06-06',
    assignedTo: ['3', '4'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w17-3',
    title: 'Evidências Técnicas',
    startDate: '2026-06-01',
    dueDate: '2026-06-06',
    assignedTo: ['4', '5'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Média'
  },
  {
    id: 'w17-4',
    title: 'Apresentação do SR2',
    startDate: '2026-06-06',
    dueDate: '2026-06-06',
    assignedTo: ['1', '2', '3', '4', '5'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.SR2,
    priority: 'Alta'
  },

  // SEMANA 18: SR2 (08/06 - 13/06)
  {
    id: 'w18-1',
    title: 'Apresentação Final SR2',
    startDate: '2026-06-08',
    dueDate: '2026-06-13',
    assignedTo: ['1', '2', '3', '4', '5'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.SR2,
    priority: 'Alta'
  },
  {
    id: 'w18-2',
    title: 'Demonstração Completa',
    startDate: '2026-06-08',
    dueDate: '2026-06-13',
    assignedTo: ['3', '4'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.TECNICO,
    priority: 'Alta'
  },
  {
    id: 'w18-3',
    title: 'Retrospectiva',
    startDate: '2026-06-08',
    dueDate: '2026-06-13',
    assignedTo: ['1', '2', '3', '4', '5'],
    columnId: ColumnId.BACKLOG_SR2,
    category: TaskCategory.PROCESSO,
    priority: 'Média'
  }
];

export const TEAM_MEMBERS: TeamMember[] = [
  { id: '1', name: 'Membro 1', role: 'Scrum Master', photo: 'https://picsum.photos/seed/member1/200' },
  { id: '2', name: 'Membro 2', role: 'Product Owner', photo: 'https://picsum.photos/seed/member2/200' },
  { id: '3', name: 'Membro 3', role: 'Dev Backend', photo: 'https://picsum.photos/seed/member3/200' },
  { id: '4', name: 'Membro 4', role: 'Dev Frontend', photo: 'https://picsum.photos/seed/member4/200' },
  { id: '5', name: 'Membro 5', role: 'QA / Tester', photo: 'https://picsum.photos/seed/member5/200' },
];

export const COLUMNS = [
  { id: ColumnId.BACKLOG_SR1, title: 'Backlog SR1' },
  { id: ColumnId.BACKLOG_SR2, title: 'Backlog SR2' },
  { id: ColumnId.DOING, title: 'Fazendo' },
  { id: ColumnId.REVIEW, title: 'Revisão/Testes' },
  { id: ColumnId.DONE, title: 'Concluído' }
];

export const INITIAL_DOCS: Record<string, string> = {
  'Project Model Canvas': '### Project Model Canvas\n\n**Justificativa:** \n**Objetivo:** \n**Benefícios:** \n**Produto:** \n**Requisitos:** ',
  'Personas & Stakeholders': '### Personas\n\n- **Persona 1:** Nome, idade, dores e necessidades.\n\n### Stakeholders\n\n- **Professor:** Orientador\n- **Equipe:** Desenvolvedores',
  'Descrição da Problemática': '### O Problema\n\nDescreva aqui o desafio que motivou o projeto...',
  'Análise de Concorrentes': '### Concorrentes\n\n| Nome | Pontos Fortes | Pontos Fracos |\n|------|---------------|---------------|\n| App A | Interface | Preço |\n| App B | Funcionalidade | UX |',
  'Protótipo Figma': 'https://figma.com/...',
  'Análise SWOT': '### SWOT\n\n- **Forças:** \n- **Fraquezas:** \n- **Oportunidades:** \n- **Ameaças:** '
};
