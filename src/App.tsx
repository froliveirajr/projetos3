import React, { useState, useMemo, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { 
  LayoutDashboard, 
  Calendar, 
  CheckCircle2, 
  Clock, 
  AlertCircle, 
  Plus, 
  ChevronRight, 
  ChevronLeft,
  Filter,
  MoreVertical,
  Github,
  Trello,
  BookOpen,
  X,
  Users,
  FileText,
  Trash2,
  Edit2,
  ExternalLink,
  Target,
  Users2,
  Zap,
  Briefcase
} from 'lucide-react';
import { 
  DndContext, 
  closestCenter,
  KeyboardSensor,
  PointerSensor,
  useSensor,
  useSensors,
  DragEndEvent,
  useDroppable
} from '@dnd-kit/core';
import {
  arrayMove,
  SortableContext,
  sortableKeyboardCoordinates,
  verticalListSortingStrategy,
  useSortable
} from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import Markdown from 'react-markdown';
import { Task, ColumnId, TaskCategory, TeamMember } from './types';
import { INITIAL_TASKS, COLUMNS, TEAM_MEMBERS } from './constants';

type AppTab = 'kanban' | 'timeline' | 'docs' | 'team';

export default function App() {
  const [tasks, setTasks] = useState<Task[]>(INITIAL_TASKS);
  const [team, setTeam] = useState<TeamMember[]>(TEAM_MEMBERS);
  const [activeTab, setActiveTab] = useState<AppTab>('kanban');
  const [filter, setFilter] = useState<TaskCategory | 'Todos'>('Todos');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loginData, setLoginData] = useState({ user: '', pass: '' });
  
  // Documentation state
  const [docs, setDocs] = useState<Record<string, string>>({
    'Project Model Canvas': '### Project Model Canvas\n\n**Justificativa:** \n**Objetivo:** \n**Benefícios:** \n**Produto:** \n**Requisitos:** ',
    'Personas & Stakeholders': '### Personas\n\n- **Persona 1:** Nome, idade, dores e necessidades.\n\n### Stakeholders\n\n- **Professor:** Orientador\n- **Equipe:** Desenvolvedores',
    'Descrição da Problemática': '### O Problema\n\nDescreva aqui o desafio que motivou o projeto...',
    'Análise de Concorrentes': '### Concorrentes\n\n| Nome | Pontos Fortes | Pontos Fracos |\n|------|---------------|---------------|\n| App A | Interface | Preço |\n| App B | Funcionalidade | UX |',
    'Protótipo Figma': 'https://figma.com/...',
    'Análise SWOT': '### SWOT\n\n- **Forças:** \n- **Fraquezas:** \n- **Oportunidades:** \n- **Ameaças:** '
  });

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDocModalOpen, setIsDocModalOpen] = useState(false);
  const [isTeamModalOpen, setIsTeamModalOpen] = useState(false);
  const [showTaskDeleteConfirm, setShowTaskDeleteConfirm] = useState(false);
  const [showMemberDeleteConfirm, setShowMemberDeleteConfirm] = useState(false);
  
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [activeDoc, setActiveDoc] = useState<{ title: string, content: string } | null>(null);
  const [docMode, setDocMode] = useState<'edit' | 'preview'>('preview');
  const [editingMember, setEditingMember] = useState<TeamMember | null>(null);
  const [confirmDeleteId, setConfirmDeleteId] = useState<string | null>(null);
  const [toast, setToast] = useState<{ message: string, type: 'success' | 'info' | 'error' } | null>(null);
  const socketRef = useRef<WebSocket | null>(null);

  // WebSocket Sync
  useEffect(() => {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const socket = new WebSocket(`${protocol}//${window.location.host}`);

    socket.onmessage = (event) => {
      const message = JSON.parse(event.data);
      switch (message.type) {
        case 'INIT':
          setTasks(message.payload.tasks);
          setTeam(message.payload.team);
          setDocs(message.payload.documents);
          break;
        case 'TASKS_UPDATED':
          setTasks(message.payload);
          break;
        case 'TEAM_UPDATED':
          setTeam(message.payload);
          break;
        case 'DOCS_UPDATED':
          setDocs(message.payload);
          break;
      }
    };

    socketRef.current = socket;

    return () => {
      socket.close();
    };
  }, []);

  const syncTasks = (newTasks: Task[]) => {
    setTasks(newTasks);
    if (socketRef.current?.readyState === WebSocket.OPEN) {
      socketRef.current.send(JSON.stringify({ type: 'UPDATE_TASKS', payload: newTasks }));
    }
  };

  const syncTeam = (newTeam: TeamMember[]) => {
    setTeam(newTeam);
    if (socketRef.current?.readyState === WebSocket.OPEN) {
      socketRef.current.send(JSON.stringify({ type: 'UPDATE_TEAM', payload: newTeam }));
    }
  };

  const syncDocs = (newDocs: Record<string, string>) => {
    setDocs(newDocs);
    if (socketRef.current?.readyState === WebSocket.OPEN) {
      socketRef.current.send(JSON.stringify({ type: 'UPDATE_DOCS', payload: newDocs }));
    }
  };

  const showToast = (message: string, type: 'success' | 'info' | 'error' = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3000);
  };

  const [newTask, setNewTask] = useState<Partial<Task>>({
    title: '',
    description: '',
    poFeedback: '',
    userStories: [],
    category: TaskCategory.PROCESSO,
    priority: 'Média',
    dueDate: new Date().toISOString().split('T')[0],
    columnId: ColumnId.BACKLOG_SR1
  });

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 8,
      },
    }),
    useSensor(KeyboardSensor, {
      coordinateGetter: sortableKeyboardCoordinates,
    })
  );

  const filteredTasks = useMemo(() => {
    if (filter === 'Todos') return tasks;
    return tasks.filter(t => t.category === filter);
  }, [tasks, filter]);

  const handleAddTask = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTask.title || !newTask.dueDate) return;

    if (editingTask) {
      const updatedTasks = tasks.map(t => t.id === editingTask.id ? { ...t, ...newTask } as Task : t);
      syncTasks(updatedTasks);
      showToast('Tarefa atualizada com sucesso');
    } else {
      const task: Task = {
        id: Math.random().toString(36).substr(2, 9),
        title: newTask.title || '',
        description: newTask.description,
        poFeedback: newTask.poFeedback,
        userStories: newTask.userStories,
        category: newTask.category as TaskCategory,
        priority: newTask.priority as 'Baixa' | 'Média' | 'Alta',
        dueDate: newTask.dueDate || '',
        columnId: newTask.columnId as ColumnId,
      };
      syncTasks([...tasks, task]);
      showToast('Nova tarefa criada');
    }
    
    closeModal();
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setEditingTask(null);
    setShowTaskDeleteConfirm(false);
    setNewTask({
      title: '',
      description: '',
      poFeedback: '',
      userStories: [],
      category: TaskCategory.PROCESSO,
      priority: 'Média',
      dueDate: new Date().toISOString().split('T')[0],
      columnId: ColumnId.BACKLOG_SR1
    });
  };

  const openEditModal = (task: Task) => {
    setEditingTask(task);
    setNewTask(task);
    setIsModalOpen(true);
  };

  const deleteTask = (id: string) => {
    syncTasks(tasks.filter(t => t.id !== id));
    setConfirmDeleteId(null);
    showToast('Tarefa excluída', 'info');
  };

  const handleDragEnd = (event: DragEndEvent) => {
    const { active, over } = event;
    if (!over) return;

    const activeId = active.id as string;
    const overId = over.id as string;

    // If dropped over a column
    if (COLUMNS.some(c => c.id === overId)) {
      syncTasks(tasks.map(t => t.id === activeId ? { ...t, columnId: overId as ColumnId } : t));
      return;
    }

    // If dropped over another task
    const activeIndex = tasks.findIndex(t => t.id === activeId);
    const overIndex = tasks.findIndex(t => t.id === overId);

    if (activeIndex !== overIndex) {
      const activeTask = tasks[activeIndex];
      const overTask = tasks[overIndex];

      if (activeTask.columnId !== overTask.columnId) {
        // Move to different column
        syncTasks(tasks.map(t => t.id === activeId ? { ...t, columnId: overTask.columnId } : t));
      } else {
        // Sort within same column
        syncTasks(arrayMove(tasks, activeIndex, overIndex));
      }
    }
  };

  const handleSaveDoc = (e: React.FormEvent) => {
    e.preventDefault();
    if (activeDoc) {
      syncDocs({ ...docs, [activeDoc.title]: activeDoc.content });
      setIsDocModalOpen(false);
    }
  };

  const handleSaveMember = (e: React.FormEvent) => {
    e.preventDefault();
    if (editingMember) {
      if (team.some(m => m.id === editingMember.id)) {
        syncTeam(team.map(m => m.id === editingMember.id ? editingMember : m));
        showToast('Membro atualizado');
      } else {
        syncTeam([...team, editingMember]);
        showToast('Novo membro adicionado');
      }
      setIsTeamModalOpen(false);
      setEditingMember(null);
    }
  };

  const deleteMember = (id: string) => {
    syncTeam(team.filter(m => m.id !== id));
    setConfirmDeleteId(null);
    showToast('Membro removido da equipe', 'info');
  };

  const handlePhotoUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file && editingMember) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setEditingMember({ ...editingMember, photo: reader.result as string });
      };
      reader.readAsDataURL(file);
    }
  };

  const handleDocImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file && activeDoc) {
      const reader = new FileReader();
      reader.onloadend = () => {
        const imgTag = `\n\n![Imagem](${reader.result})\n\n`;
        setActiveDoc({ ...activeDoc, content: activeDoc.content + imgTag });
      };
      reader.readAsDataURL(file);
    }
  };

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    if (loginData.user && loginData.pass) {
      setIsLoggedIn(true);
    }
  };

  const progress = useMemo(() => {
    const done = tasks.filter(t => t.columnId === ColumnId.DONE).length;
    return tasks.length > 0 ? Math.round((done / tasks.length) * 100) : 0;
  }, [tasks]);

  if (!isLoggedIn) {
    return (
      <div className="min-h-screen bg-indigo-600 flex items-center justify-center p-4">
        <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="bg-white p-8 rounded-3xl shadow-2xl w-full max-w-md">
          <div className="text-center mb-8">
            <div className="bg-indigo-100 w-16 h-16 rounded-2xl flex items-center justify-center mx-auto mb-4">
              <LayoutDashboard className="text-indigo-600 w-8 h-8" />
            </div>
            <h1 className="text-2xl font-bold text-gray-900">Projetos 3 - Equipe 1</h1>
            <p className="text-gray-500 text-sm">Entre para gerenciar seu projeto</p>
          </div>
          <form onSubmit={handleLogin} className="space-y-4">
            <div>
              <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Usuário / E-mail</label>
              <input type="text" required value={loginData.user} onChange={e => setLoginData(prev => ({ ...prev, user: e.target.value }))} className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="membro@equipe.com" />
            </div>
            <div>
              <label className="block text-xs font-bold text-gray-500 uppercase mb-1">Senha</label>
              <input type="password" required value={loginData.pass} onChange={e => setLoginData(prev => ({ ...prev, pass: e.target.value }))} className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="••••••••" />
            </div>
            <button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 rounded-xl transition-all shadow-lg shadow-indigo-200">Acessar Sistema</button>
          </form>
          <div className="mt-6 text-center">
            <p className="text-xs text-gray-400">Acesso restrito a alunos e professores</p>
          </div>
        </motion.div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#F8F9FA] text-[#1A1A1A] font-sans">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 sticky top-0 z-30">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center gap-3">
              <div className="bg-indigo-600 p-2 rounded-lg">
                <LayoutDashboard className="text-white w-6 h-6" />
              </div>
              <div>
                <h1 className="text-lg font-bold tracking-tight">Projetos 3 - Equipe 1</h1>
                <p className="text-xs text-gray-500 font-medium uppercase tracking-wider">Metodologias Ágeis • Spring Boot</p>
              </div>
            </div>
            
            <div className="flex items-center gap-4">
              <div className="hidden md:flex items-center gap-2 bg-gray-100 px-3 py-1.5 rounded-full">
                <div className="w-24 h-2 bg-gray-200 rounded-full overflow-hidden">
                  <motion.div 
                    initial={{ width: 0 }}
                    animate={{ width: `${progress}%` }}
                    className="h-full bg-indigo-600"
                  />
                </div>
                <span className="text-xs font-bold text-gray-600">{progress}%</span>
              </div>
              
              <nav className="flex bg-gray-100 p-1 rounded-lg">
                <TabButton active={activeTab === 'kanban'} onClick={() => setActiveTab('kanban')} icon={<LayoutDashboard className="w-4 h-4" />} label="Kanban" />
                <TabButton active={activeTab === 'timeline'} onClick={() => setActiveTab('timeline')} icon={<Calendar className="w-4 h-4" />} label="Cronograma" />
                <TabButton active={activeTab === 'docs'} onClick={() => setActiveTab('docs')} icon={<FileText className="w-4 h-4" />} label="Docs" />
                <TabButton active={activeTab === 'team'} onClick={() => setActiveTab('team')} icon={<Users className="w-4 h-4" />} label="Equipe" />
              </nav>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <AnimatePresence mode="wait">
          {activeTab === 'kanban' && (
            <motion.div key="kanban" initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -10 }}>
              <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
                <div className="flex items-center gap-2 overflow-x-auto pb-2 sm:pb-0 w-full sm:w-auto">
                  <Filter className="w-4 h-4 text-gray-400 shrink-0" />
                  {(['Todos', ...Object.values(TaskCategory)] as const).map((cat) => (
                    <button key={cat} onClick={() => setFilter(cat)} className={`px-3 py-1 rounded-full text-xs font-semibold whitespace-nowrap transition-colors ${filter === cat ? 'bg-indigo-100 text-indigo-700' : 'bg-white border border-gray-200 text-gray-600 hover:bg-gray-50'}`}>
                      {cat}
                    </button>
                  ))}
                </div>
                <button onClick={() => setIsModalOpen(true)} className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg text-sm font-semibold transition-all shadow-sm">
                  <Plus className="w-4 h-4" /> Nova Tarefa
                </button>
              </div>

              <DndContext sensors={sensors} collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
                <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-5 gap-6">
                  {COLUMNS.map((column) => (
                    <KanbanColumn 
                      key={column.id} 
                      column={column} 
                      tasks={filteredTasks.filter(t => t.columnId === column.id)} 
                      onEdit={openEditModal}
                      onDelete={deleteTask}
                      confirmDeleteId={confirmDeleteId}
                      setConfirmDeleteId={setConfirmDeleteId}
                    />
                  ))}
                </div>
              </DndContext>
            </motion.div>
          )}

          {activeTab === 'timeline' && (
            <motion.div key="timeline" initial={{ opacity: 0, scale: 0.98 }} animate={{ opacity: 1, scale: 1 }} exit={{ opacity: 0, scale: 1.02 }} className="bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
              <div className="p-6 border-b border-gray-100 bg-gray-50/50">
                <h2 className="text-xl font-bold">Linha do Tempo do Projeto</h2>
                <p className="text-sm text-gray-500">Marcos de entrega e prazos críticos</p>
              </div>
              <div className="p-8">
                <div className="relative">
                  <div className="absolute left-4 top-0 bottom-0 w-0.5 bg-gray-200 md:left-1/2 md:-ml-px" />
                  <div className="space-y-12">
                    {tasks
                      .sort((a, b) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime())
                      .filter((t, i, self) => self.findIndex(x => x.dueDate === t.dueDate) === i)
                      .map((task, idx) => (
                        <TimelineItem key={task.dueDate} task={task} idx={idx} tasks={tasks} />
                      ))}
                  </div>
                </div>
              </div>
            </motion.div>
          )}

          {activeTab === 'docs' && (
            <motion.div key="docs" initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: -20 }} className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {(Object.entries(docs) as [string, string][]).map(([title, content]) => (
                <DocCard 
                  key={title}
                  title={title} 
                  icon={getDocIcon(title)} 
                  description={content.substring(0, 100) + (content.length > 100 ? '...' : '')}
                  onClick={() => {
                    setActiveDoc({ title, content });
                    setIsDocModalOpen(true);
                  }}
                />
              ))}
            </motion.div>
          )}

          {activeTab === 'team' && (
            <motion.div key="team" initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -20 }} className="space-y-6">
              <div className="flex justify-end">
                <button 
                  onClick={() => {
                    setEditingMember({ id: Math.random().toString(36).substr(2, 9), name: '', role: '', photo: 'https://picsum.photos/seed/user/200' });
                    setIsTeamModalOpen(true);
                  }}
                  className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg text-sm font-semibold transition-all shadow-sm"
                >
                  <Plus className="w-4 h-4" /> Adicionar Membro
                </button>
              </div>
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6">
                {team.map(member => (
                  <div key={member.id} className="bg-white p-6 rounded-2xl border border-gray-200 shadow-sm text-center hover:shadow-md transition-all group relative">
                    <div className="absolute top-4 right-4 flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                      {confirmDeleteId === member.id ? (
                        <button 
                          onClick={(e) => { e.stopPropagation(); deleteMember(member.id); }}
                          className="p-2 bg-red-600 text-white rounded-full hover:bg-red-700 animate-pulse"
                          title="Confirmar exclusão"
                        >
                          <Trash2 className="w-4 h-4" />
                        </button>
                      ) : (
                        <>
                          <button 
                            onClick={() => {
                              setEditingMember(member);
                              setIsTeamModalOpen(true);
                            }}
                            className="p-2 bg-gray-100 rounded-full hover:bg-indigo-50 hover:text-indigo-600"
                          >
                            <Edit2 className="w-4 h-4" />
                          </button>
                          <button 
                            onClick={(e) => { e.stopPropagation(); setConfirmDeleteId(member.id); setTimeout(() => setConfirmDeleteId(null), 3000); }}
                            className="p-2 bg-gray-100 rounded-full hover:bg-red-50 hover:text-red-600"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </>
                      )}
                    </div>
                    <img src={member.photo} alt={member.name} className="w-24 h-24 rounded-full mx-auto mb-4 border-4 border-indigo-50 object-cover" referrerPolicy="no-referrer" />
                    <h3 className="font-bold text-gray-900">{member.name}</h3>
                    <p className="text-sm text-indigo-600 font-medium">{member.role}</p>
                  </div>
                ))}
              </div>
            </motion.div>
          )}
        </AnimatePresence>

        {/* Task Modal */}
        <AnimatePresence>
          {isModalOpen && (
            <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm">
              <motion.div initial={{ opacity: 0, scale: 0.95, y: 20 }} animate={{ opacity: 1, scale: 1, y: 0 }} exit={{ opacity: 0, scale: 0.95, y: 20 }} className="bg-white rounded-2xl shadow-xl w-full max-w-2xl overflow-hidden max-h-[90vh] flex flex-col">
                <div className="p-6 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
                  <h2 className="text-xl font-bold">{editingTask ? 'Editar Tarefa' : 'Nova Tarefa'}</h2>
                  <button onClick={closeModal} className="p-2 hover:bg-gray-200 rounded-full transition-colors"><X className="w-5 h-5 text-gray-400" /></button>
                </div>
                
                <form onSubmit={handleAddTask} className="p-6 space-y-6 overflow-y-auto">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-4">
                      <div>
                        <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">Título</label>
                        <input type="text" required value={newTask.title} onChange={e => setNewTask(prev => ({ ...prev, title: e.target.value }))} className="w-full px-4 py-2 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="Ex: Implementar Login" />
                      </div>
                      <div>
                        <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">Descrição</label>
                        <textarea rows={3} value={newTask.description} onChange={e => setNewTask(prev => ({ ...prev, description: e.target.value }))} className="w-full px-4 py-2 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none resize-none" placeholder="Detalhes da tarefa..." />
                      </div>
                      <div>
                        <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">Fala do P.O. (Feedback)</label>
                        <textarea rows={2} value={newTask.poFeedback} onChange={e => setNewTask(prev => ({ ...prev, poFeedback: e.target.value }))} className="w-full px-4 py-2 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none resize-none bg-indigo-50/50" placeholder="Feedback do Product Owner..." />
                      </div>
                      <div>
                        <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">Histórias de Usuário</label>
                        <div className="space-y-2">
                          {newTask.userStories?.map((story, i) => (
                            <div key={i} className="flex gap-2">
                              <input type="text" value={story} onChange={e => {
                                const stories = [...(newTask.userStories || [])];
                                stories[i] = e.target.value;
                                setNewTask(prev => ({ ...prev, userStories: stories }));
                              }} className="flex-1 px-3 py-1.5 text-sm rounded-lg border border-gray-200" />
                              <button type="button" onClick={() => setNewTask(prev => ({ ...prev, userStories: prev.userStories?.filter((_, idx) => idx !== i) }))} className="p-1.5 text-red-500 hover:bg-red-50 rounded-lg"><Trash2 className="w-4 h-4" /></button>
                            </div>
                          ))}
                          <button type="button" onClick={() => setNewTask(prev => ({ ...prev, userStories: [...(prev.userStories || []), ''] }))} className="text-xs font-bold text-indigo-600 hover:text-indigo-700 flex items-center gap-1"><Plus className="w-3 h-3" /> Adicionar História</button>
                        </div>
                      </div>
                    </div>

                    <div className="space-y-4">
                      <div className="grid grid-cols-2 gap-4">
                        <div>
                          <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">Categoria</label>
                          <select value={newTask.category} onChange={e => setNewTask(prev => ({ ...prev, category: e.target.value as TaskCategory }))} className="w-full px-4 py-2 rounded-xl border border-gray-200 bg-white">
                            {Object.values(TaskCategory).map(cat => <option key={cat} value={cat}>{cat}</option>)}
                          </select>
                        </div>
                        <div>
                          <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">Prioridade</label>
                          <select value={newTask.priority} onChange={e => setNewTask(prev => ({ ...prev, priority: e.target.value as any }))} className="w-full px-4 py-2 rounded-xl border border-gray-200 bg-white">
                            <option value="Baixa">Baixa</option><option value="Média">Média</option><option value="Alta">Alta</option>
                          </select>
                        </div>
                      </div>
                      <div>
                        <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">Data de Entrega</label>
                        <input type="date" required value={newTask.dueDate} onChange={e => setNewTask(prev => ({ ...prev, dueDate: e.target.value }))} className="w-full px-4 py-2 rounded-xl border border-gray-200" />
                      </div>
                      <div>
                        <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">Coluna</label>
                        <select value={newTask.columnId} onChange={e => setNewTask(prev => ({ ...prev, columnId: e.target.value as ColumnId }))} className="w-full px-4 py-2 rounded-xl border border-gray-200 bg-white">
                          {COLUMNS.map(col => <option key={col.id} value={col.id}>{col.title}</option>)}
                        </select>
                      </div>
                    </div>
                  </div>
                  
                  <div className="pt-6 border-t border-gray-100 flex flex-col gap-3">
                    {editingTask && (
                      <div className="flex flex-col gap-2">
                        {!showTaskDeleteConfirm ? (
                          <button 
                            type="button" 
                            onClick={() => setShowTaskDeleteConfirm(true)} 
                            className="w-full px-4 py-2 rounded-xl border border-red-200 text-red-600 font-semibold hover:bg-red-50 flex items-center justify-center gap-2 transition-colors"
                          >
                            <Trash2 className="w-4 h-4" /> Excluir Tarefa
                          </button>
                        ) : (
                          <div className="flex items-center gap-2 p-2 bg-red-50 rounded-xl border border-red-100 animate-pulse">
                            <span className="text-xs font-bold text-red-700 flex-1 ml-2">Confirmar exclusão?</span>
                            <button 
                              type="button" 
                              onClick={() => {
                                deleteTask(editingTask.id);
                                closeModal();
                              }} 
                              className="px-3 py-1 bg-red-600 text-white text-xs font-bold rounded-lg hover:bg-red-700"
                            >
                              Sim, Excluir
                            </button>
                            <button 
                              type="button" 
                              onClick={() => setShowTaskDeleteConfirm(false)} 
                              className="px-3 py-1 bg-white text-gray-600 text-xs font-bold rounded-lg border border-gray-200"
                            >
                              Não
                            </button>
                          </div>
                        )}
                      </div>
                    )}
                    <div className="flex gap-3">
                      <button type="button" onClick={closeModal} className="flex-1 px-4 py-2 rounded-xl border border-gray-200 text-gray-600 font-semibold hover:bg-gray-50">Cancelar</button>
                      <button type="submit" className="flex-1 px-4 py-2 rounded-xl bg-indigo-600 text-white font-semibold hover:bg-indigo-700 shadow-sm">{editingTask ? 'Salvar Alterações' : 'Criar Tarefa'}</button>
                    </div>
                  </div>
                </form>
              </motion.div>
            </div>
          )}
        </AnimatePresence>

        {/* Doc Modal */}
        <AnimatePresence>
          {isDocModalOpen && activeDoc && (
            <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm">
              <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} exit={{ opacity: 0, scale: 0.95 }} className="bg-white rounded-2xl shadow-xl w-full max-w-3xl overflow-hidden flex flex-col max-h-[90vh]">
                <div className="p-6 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
                  <div className="flex flex-col">
                    <h2 className="text-xl font-bold">{activeDoc.title}</h2>
                    <div className="flex gap-2 mt-2">
                      <button onClick={() => setDocMode('preview')} className={`px-3 py-1 rounded-lg text-xs font-bold transition-all ${docMode === 'preview' ? 'bg-indigo-600 text-white shadow-sm' : 'bg-white text-gray-500 hover:bg-gray-100'}`}>Visualizar</button>
                      <button onClick={() => setDocMode('edit')} className={`px-3 py-1 rounded-lg text-xs font-bold transition-all ${docMode === 'edit' ? 'bg-indigo-600 text-white shadow-sm' : 'bg-white text-gray-500 hover:bg-gray-100'}`}>Editar</button>
                    </div>
                  </div>
                  <button onClick={() => setIsDocModalOpen(false)} className="p-2 hover:bg-gray-200 rounded-full transition-colors"><X className="w-5 h-5 text-gray-400" /></button>
                </div>
                <div className="p-6 flex flex-col gap-4 overflow-y-auto flex-1">
                  {docMode === 'edit' ? (
                    <>
                      <div className="flex gap-2 mb-2">
                        <button type="button" onClick={() => setActiveDoc(prev => prev ? { ...prev, content: prev.content + '\n\n### Novo Template\n- Item 1\n- Item 2' } : null)} className="text-[10px] font-bold px-2 py-1 bg-gray-100 rounded hover:bg-gray-200 transition-colors">Inserir Checklist</button>
                        <button type="button" onClick={() => setActiveDoc(prev => prev ? { ...prev, content: prev.content + '\n\n| Coluna 1 | Coluna 2 |\n|----------|----------|\n| Dado 1 | Dado 2 |' } : null)} className="text-[10px] font-bold px-2 py-1 bg-gray-100 rounded hover:bg-gray-200 transition-colors">Inserir Tabela</button>
                        <label className="cursor-pointer text-[10px] font-bold px-2 py-1 bg-indigo-50 text-indigo-600 rounded hover:bg-indigo-100 transition-colors flex items-center gap-1">
                          <Plus className="w-3 h-3" /> Inserir Imagem / Template
                          <input type="file" className="hidden" accept="image/*" onChange={handleDocImageUpload} />
                        </label>
                      </div>
                      <textarea 
                        value={activeDoc.content} 
                        onChange={e => setActiveDoc(prev => prev ? { ...prev, content: e.target.value } : null)}
                        className="w-full flex-1 min-h-[400px] p-4 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none resize-none font-mono text-sm"
                        placeholder="Escreva aqui o conteúdo do documento..."
                      />
                    </>
                  ) : (
                    <div className="prose prose-sm max-w-none prose-indigo bg-gray-50/50 p-6 rounded-2xl border border-gray-100 min-h-[400px]">
                      <Markdown>{activeDoc.content}</Markdown>
                    </div>
                  )}
                  <div className="flex gap-3 mt-4">
                    <button type="button" onClick={() => setIsDocModalOpen(false)} className="flex-1 px-4 py-2 rounded-xl border border-gray-200 text-gray-600 font-semibold">Fechar</button>
                    {docMode === 'edit' && (
                      <button type="button" onClick={handleSaveDoc} className="flex-1 px-4 py-2 rounded-xl bg-indigo-600 text-white font-semibold">Salvar Documento</button>
                    )}
                  </div>
                </div>
              </motion.div>
            </div>
          )}
        </AnimatePresence>

        {/* Team Modal */}
        <AnimatePresence>
          {isTeamModalOpen && editingMember && (
            <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm">
              <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} exit={{ opacity: 0, scale: 0.95 }} className="bg-white rounded-2xl shadow-xl w-full max-w-md overflow-hidden">
                <div className="p-6 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
                  <h2 className="text-xl font-bold">Editar Membro</h2>
                  <button onClick={() => setIsTeamModalOpen(false)} className="p-2 hover:bg-gray-200 rounded-full transition-colors"><X className="w-5 h-5 text-gray-400" /></button>
                </div>
                <form onSubmit={handleSaveMember} className="p-6 space-y-4">
                  <div>
                    <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">Nome</label>
                    <input type="text" required value={editingMember.name} onChange={e => setEditingMember(prev => prev ? { ...prev, name: e.target.value } : null)} className="w-full px-4 py-2 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none" />
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">Função</label>
                    <input type="text" required value={editingMember.role} onChange={e => setEditingMember(prev => prev ? { ...prev, role: e.target.value } : null)} className="w-full px-4 py-2 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none" />
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-gray-500 uppercase tracking-wider mb-1">URL da Foto</label>
                    <div className="flex gap-2">
                      <input type="text" required value={editingMember.photo} onChange={e => setEditingMember(prev => prev ? { ...prev, photo: e.target.value } : null)} className="flex-1 px-4 py-2 rounded-xl border border-gray-200 focus:ring-2 focus:ring-indigo-500 outline-none" />
                      <label className="cursor-pointer bg-gray-100 hover:bg-gray-200 p-2 rounded-xl flex items-center justify-center border border-gray-200">
                        <Plus className="w-5 h-5 text-gray-500" />
                        <input type="file" className="hidden" accept="image/*" onChange={handlePhotoUpload} />
                      </label>
                    </div>
                    <p className="text-[10px] text-gray-400 mt-1">Cole uma URL ou clique no "+" para fazer upload</p>
                  </div>
                  <div className="pt-4 flex flex-col gap-3">
                    {team.some(m => m.id === editingMember.id) && (
                      <div className="flex flex-col gap-2">
                        {!showMemberDeleteConfirm ? (
                          <button 
                            type="button" 
                            onClick={() => setShowMemberDeleteConfirm(true)} 
                            className="w-full px-4 py-2 rounded-xl border border-red-200 text-red-600 font-semibold hover:bg-red-50 flex items-center justify-center gap-2 transition-colors"
                          >
                            <Trash2 className="w-4 h-4" /> Remover Membro
                          </button>
                        ) : (
                          <div className="flex items-center gap-2 p-2 bg-red-50 rounded-xl border border-red-100">
                            <span className="text-xs font-bold text-red-700 flex-1 ml-2">Remover da equipe?</span>
                            <button 
                              type="button" 
                              onClick={() => {
                                deleteMember(editingMember.id);
                                setIsTeamModalOpen(false);
                                setShowMemberDeleteConfirm(false);
                              }} 
                              className="px-3 py-1 bg-red-600 text-white text-xs font-bold rounded-lg hover:bg-red-700"
                            >
                              Remover
                            </button>
                            <button 
                              type="button" 
                              onClick={() => setShowMemberDeleteConfirm(false)} 
                              className="px-3 py-1 bg-white text-gray-600 text-xs font-bold rounded-lg border border-gray-200"
                            >
                              Não
                            </button>
                          </div>
                        )}
                      </div>
                    )}
                    <div className="flex gap-3">
                      <button type="button" onClick={() => {
                        setIsTeamModalOpen(false);
                        setShowMemberDeleteConfirm(false);
                      }} className="flex-1 px-4 py-2 rounded-xl border border-gray-200 text-gray-600 font-semibold">Cancelar</button>
                      <button type="submit" className="flex-1 px-4 py-2 rounded-xl bg-indigo-600 text-white font-semibold">Salvar</button>
                    </div>
                  </div>
                </form>
              </motion.div>
            </div>
          )}
        </AnimatePresence>
        {/* Toast Notification */}
        <AnimatePresence>
          {toast && (
            <motion.div 
              initial={{ opacity: 0, y: 50 }} 
              animate={{ opacity: 1, y: 0 }} 
              exit={{ opacity: 0, y: 50 }} 
              className={`fixed bottom-8 left-1/2 -translate-x-1/2 z-[100] px-6 py-3 rounded-2xl shadow-2xl flex items-center gap-3 border ${
                toast.type === 'success' ? 'bg-emerald-600 border-emerald-500 text-white' : 
                toast.type === 'error' ? 'bg-red-600 border-red-500 text-white' : 
                'bg-gray-900 border-gray-800 text-white'
              }`}
            >
              {toast.type === 'success' && <CheckCircle2 className="w-5 h-5" />}
              {toast.type === 'error' && <AlertCircle className="w-5 h-5" />}
              {toast.type === 'info' && <Trash2 className="w-5 h-5" />}
              <span className="font-bold text-sm">{toast.message}</span>
            </motion.div>
          )}
        </AnimatePresence>
      </main>
    </div>
  );
}

function TabButton({ active, onClick, icon, label }: { active: boolean, onClick: () => void, icon: React.ReactNode, label: string }) {
  return (
    <button onClick={onClick} className={`flex items-center gap-2 px-4 py-1.5 rounded-md text-sm font-medium transition-all ${active ? 'bg-white shadow-sm text-indigo-600' : 'text-gray-500 hover:text-gray-700'}`}>
      {icon} <span className="hidden sm:inline">{label}</span>
    </button>
  );
}

const KanbanColumn: React.FC<{ column: any, tasks: Task[], onEdit: (t: Task) => void, onDelete: (id: string) => void, confirmDeleteId: string | null, setConfirmDeleteId: (id: string | null) => void }> = ({ column, tasks, onEdit, onDelete, confirmDeleteId, setConfirmDeleteId }) => {
  const { setNodeRef } = useDroppable({
    id: column.id,
  });

  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center justify-between px-2">
        <div className="flex items-center gap-2">
          <h2 className="text-sm font-bold text-gray-700 uppercase tracking-wider">{column.title}</h2>
          <span className="bg-gray-200 text-gray-600 text-[10px] font-bold px-1.5 py-0.5 rounded-full">{tasks.length}</span>
        </div>
      </div>
      
      <SortableContext items={tasks.map(t => t.id)} strategy={verticalListSortingStrategy}>
        <div ref={setNodeRef} className="flex flex-col gap-3 min-h-[500px] bg-gray-100/50 p-2 rounded-xl border border-dashed border-gray-300">
          {tasks.map((task) => (
            <SortableTaskCard 
              key={task.id} 
              task={task} 
              onEdit={onEdit} 
              onDelete={onDelete} 
              isConfirmingDelete={confirmDeleteId === task.id}
              setConfirmDeleteId={setConfirmDeleteId}
            />
          ))}
        </div>
      </SortableContext>
    </div>
  );
};

function getDocIcon(title: string) {
  switch (title) {
    case 'Project Model Canvas': return <Target className="w-6 h-6 text-orange-500" />;
    case 'Personas & Stakeholders': return <Users2 className="w-6 h-6 text-blue-500" />;
    case 'Descrição da Problemática': return <AlertCircle className="w-6 h-6 text-red-500" />;
    case 'Análise de Concorrentes': return <Zap className="w-6 h-6 text-yellow-500" />;
    case 'Protótipo Figma': return <LayoutDashboard className="w-6 h-6 text-purple-500" />;
    case 'Análise SWOT': return <Briefcase className="w-6 h-6 text-emerald-500" />;
    default: return <FileText className="w-6 h-6 text-gray-500" />;
  }
}

const SortableTaskCard: React.FC<{ 
  task: Task, 
  onEdit: (t: Task) => void, 
  onDelete: (id: string) => void,
  isConfirmingDelete: boolean,
  setConfirmDeleteId: (id: string | null) => void
}> = ({ task, onEdit, onDelete, isConfirmingDelete, setConfirmDeleteId }) => {
  const { attributes, listeners, setNodeRef, transform, transition, isDragging } = useSortable({ id: task.id });
  
  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
    zIndex: isDragging ? 50 : 1,
    opacity: isDragging ? 0.5 : 1,
  };

  const priorityColor = { 'Baixa': 'bg-blue-100 text-blue-700', 'Média': 'bg-yellow-100 text-yellow-700', 'Alta': 'bg-red-100 text-red-700' }[task.priority];
  const categoryColor = { [TaskCategory.PROCESSO]: 'bg-purple-50 text-purple-600 border-purple-100', [TaskCategory.TECNICO]: 'bg-emerald-50 text-emerald-600 border-emerald-100', [TaskCategory.KICKOFF]: 'bg-orange-50 text-orange-600 border-orange-100', [TaskCategory.SR1]: 'bg-indigo-50 text-indigo-600 border-indigo-100', [TaskCategory.SR2]: 'bg-pink-50 text-pink-600 border-pink-100' }[task.category];
  const isDone = task.columnId === ColumnId.DONE;

  return (
    <div 
      ref={setNodeRef} 
      style={style} 
      {...attributes} 
      {...listeners} 
      onClick={() => onEdit(task)}
      className={`bg-white p-4 rounded-xl border border-gray-200 shadow-sm hover:shadow-md transition-all group relative cursor-grab active:cursor-grabbing ${isDone ? 'opacity-75' : ''}`}
    >
      <div className="flex justify-between items-start mb-3">
        <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full border ${categoryColor}`}>{task.category}</span>
        <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
          {isConfirmingDelete ? (
            <button 
              onClick={(e) => { 
                e.stopPropagation(); 
                onDelete(task.id); 
              }} 
              onPointerDown={(e) => e.stopPropagation()}
              onMouseDown={(e) => e.stopPropagation()}
              className="p-1 bg-red-600 text-white rounded animate-pulse"
              title="Confirmar exclusão"
            >
              <Trash2 className="w-3 h-3" />
            </button>
          ) : (
            <>
              <button 
                onClick={(e) => { 
                  e.stopPropagation(); 
                  onEdit(task); 
                }} 
                onPointerDown={(e) => e.stopPropagation()}
                onMouseDown={(e) => e.stopPropagation()}
                className="p-1 hover:bg-gray-100 rounded text-gray-400 hover:text-indigo-600"
              >
                <Edit2 className="w-3 h-3" />
              </button>
              <button 
                onClick={(e) => { 
                  e.stopPropagation(); 
                  setConfirmDeleteId(task.id);
                  setTimeout(() => setConfirmDeleteId(null), 3000);
                }} 
                onPointerDown={(e) => e.stopPropagation()}
                onMouseDown={(e) => e.stopPropagation()}
                className="p-1 hover:bg-gray-100 rounded text-gray-400 hover:text-red-600"
              >
                <Trash2 className="w-3 h-3" />
              </button>
            </>
          )}
        </div>
      </div>
      <h3 className={`text-sm font-bold text-gray-900 mb-2 leading-tight ${isDone ? 'line-through text-gray-400' : ''}`}>{task.title}</h3>
      {task.description && <p className="text-[10px] text-gray-500 line-clamp-2 mb-3">{task.description}</p>}
      
      {task.poFeedback && (
        <div className="mb-3 p-2 bg-indigo-50 rounded-lg border border-indigo-100 relative">
          <div className="absolute -top-2 left-2 bg-indigo-600 text-white text-[8px] font-bold px-1 rounded">P.O.</div>
          <p className="text-[9px] text-indigo-700 italic leading-tight">"{task.poFeedback}"</p>
        </div>
      )}

      <div className="flex items-center justify-between mt-auto pt-3 border-t border-gray-50">
        <div className="flex items-center gap-1.5 text-gray-400">
          <Calendar className="w-3 h-3" />
          <span className="text-[10px] font-medium">{new Date(task.dueDate).toLocaleDateString('pt-BR', { day: '2-digit', month: 'short' })}</span>
        </div>
        <div className="flex items-center gap-2">
          <span className={`text-[10px] font-bold px-1.5 py-0.5 rounded ${priorityColor}`}>{task.priority}</span>
          {isDone ? <CheckCircle2 className="w-4 h-4 text-green-500" /> : <Clock className="w-4 h-4 text-gray-300" />}
        </div>
      </div>
    </div>
  );
};

const TimelineItem: React.FC<{ task: Task, idx: number, tasks: Task[] }> = ({ task, idx, tasks }) => {
  return (
    <div className="relative">
      <div className="flex flex-col md:flex-row items-center">
        <div className={`flex-1 w-full md:w-1/2 ${idx % 2 === 0 ? 'md:pr-12 md:text-right' : 'md:order-last md:pl-12'}`}>
          <div className="bg-white p-4 rounded-xl border border-gray-200 shadow-sm hover:border-indigo-300 transition-colors">
            <time className="text-xs font-bold text-indigo-600 uppercase tracking-widest block mb-1">{new Date(task.dueDate).toLocaleDateString('pt-BR', { day: '2-digit', month: 'long' })}</time>
            <h3 className="font-bold text-gray-900 mb-2">Marco {idx + 1}: {task.dueDate === '2026-06-19' ? 'Entrega Final' : 'Entrega Parcial'}</h3>
            <div className="space-y-2">
              {tasks.filter(t => t.dueDate === task.dueDate).map(t => (
                <div key={t.id} className="flex items-center gap-2 text-sm text-gray-600">
                  <div className={`w-1.5 h-1.5 rounded-full ${t.columnId === ColumnId.DONE ? 'bg-green-500' : 'bg-gray-300'}`} />
                  <span className={t.columnId === ColumnId.DONE ? 'line-through opacity-50' : ''}>{t.title}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
        <div className="absolute left-4 md:left-1/2 w-8 h-8 bg-white border-4 border-indigo-600 rounded-full -ml-4 flex items-center justify-center z-10">
          <div className="w-2 h-2 bg-indigo-600 rounded-full" />
        </div>
        <div className="flex-1 hidden md:block" />
      </div>
    </div>
  );
};

const DocCard: React.FC<{ title: string, icon: React.ReactNode, description: string, onClick: () => void }> = ({ title, icon, description, onClick }) => {
  return (
    <div onClick={onClick} className="bg-white p-6 rounded-2xl border border-gray-200 shadow-sm hover:shadow-md transition-all group cursor-pointer">
      <div className="mb-4 p-3 bg-gray-50 rounded-xl w-fit group-hover:bg-indigo-50 transition-colors">{icon}</div>
      <h3 className="font-bold text-gray-900 mb-2 flex items-center justify-between">
        {title} <ExternalLink className="w-4 h-4 text-gray-300 group-hover:text-indigo-400" />
      </h3>
      <p className="text-sm text-gray-500 leading-relaxed">{description}</p>
    </div>
  );
};
