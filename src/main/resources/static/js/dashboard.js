document.addEventListener('DOMContentLoaded', function() {
    // Configura o token JWT no header (se estiver usando autenticação)
    const token = localStorage.getItem('token');
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };

    // Logout
    document.getElementById('logoutBtn').addEventListener('click', function() {
        localStorage.removeItem('token'); // Remove o token
        window.location.href = '/login';
    });
    
    // Listar usuários
    document.getElementById('listarUsuariosBtn').addEventListener('click', function() {
        fetch('/api/usuarios', {
            headers: headers
        })
        .then(response => {
            if (!response.ok) throw new Error('Erro ao listar usuários');
            return response.json();
        })
        .then(usuarios => {
            const lista = document.getElementById('listaUsuarios');
            lista.innerHTML = '<h4>Usuários:</h4><ul>' + 
                usuarios.map(u => `<li>${u.nome} (${u.email})</li>`).join('') + 
                '</ul>';
        })
        .catch(error => {
            alert(error.message);
        });
    });
    
    // Buscar usuário por ID
    document.getElementById('buscarUsuarioBtn').addEventListener('click', function() {
        const id = document.getElementById('usuarioId').value;
        fetch(`/api/usuarios/${id}`, {
            headers: headers
        })
        .then(response => {
            if (!response.ok) throw new Error('Usuário não encontrado');
            return response.json();
        })
        .then(usuario => {
            document.getElementById('usuarioEncontrado').innerHTML = 
                `<p>Nome: ${usuario.nome}<br>Email: ${usuario.email}</p>`;
        })
        .catch(error => {
            alert(error.message);
        });
    });
    
    // Adicionar usuário
    document.getElementById('adicionarUsuarioBtn').addEventListener('click', function() {
        const usuario = {
            nome: document.getElementById('novoNome').value,
            email: document.getElementById('novoEmail').value,
            senha: document.getElementById('novaSenha').value
        };
        
        fetch('/api/usuarios', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(usuario)
        })
        .then(response => {
            if (response.ok) {
                alert('Usuário adicionado com sucesso!');
                document.getElementById('novoNome').value = '';
                document.getElementById('novoEmail').value = '';
                document.getElementById('novaSenha').value = '';
            } else {
                throw new Error('Erro ao adicionar usuário');
            }
        })
        .catch(error => {
            alert(error.message);
        });
    });
    
    // Atualizar usuário
    document.getElementById('atualizarUsuarioBtn').addEventListener('click', function() {
        const id = document.getElementById('atualizarId').value;
        const usuario = {
            nome: document.getElementById('atualizarNome').value,
            email: document.getElementById('atualizarEmail').value
        };
        
        fetch(`/api/usuarios/${id}`, {
            method: 'PUT',
            headers: headers,
            body: JSON.stringify(usuario)
        })
        .then(response => {
            if (response.ok) {
                alert('Usuário atualizado com sucesso!');
            } else {
                throw new Error('Erro ao atualizar usuário');
            }
        })
        .catch(error => {
            alert(error.message);
        });
    });
    
    // Remover usuário
    document.getElementById('removerUsuarioBtn').addEventListener('click', function() {
        const id = document.getElementById('removerId').value;
        
        if (confirm(`Tem certeza que deseja remover o usuário com ID ${id}?`)) {
            fetch(`/api/usuarios/${id}`, {
                method: 'DELETE',
                headers: headers
            })
            .then(response => {
                if (response.ok) {
                    alert('Usuário removido com sucesso!');
                } else {
                    throw new Error('Erro ao remover usuário');
                }
            })
            .catch(error => {
                alert(error.message);
            });
        }
    });
});