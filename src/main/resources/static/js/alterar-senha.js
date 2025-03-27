document.getElementById('redefinirSenhaForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const novaSenha = document.getElementById('novaSenha').value;
    const confirmacaoSenha = document.getElementById('confirmacaoSenha').value;
    const token = document.getElementById('token').value;
    const email = document.getElementById('email').value;
    const btnSubmit = document.querySelector('#redefinirSenhaForm button[type="submit"]');
    
    // Validação básica no frontend
    if (novaSenha !== confirmacaoSenha) {
        showError('As senhas não coincidem');
        return;
    }
    
    if (novaSenha.length < 6) {
        showError('A senha deve ter no mínimo 6 caracteres');
        return;
    }
    
    // Desabilita o botão para evitar múltiplos envios
    btnSubmit.disabled = true;
    btnSubmit.textContent = 'Processando...';
    
    const recuperacaoSenhaDTO = {
        token: token,
        email: email,
        novaSenha: novaSenha,
        confirmacaoSenha: confirmacaoSenha
    };
    
    fetch('/api/auth/alterar-senha', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(recuperacaoSenhaDTO)
    })
    .then(response => {
        if (response.ok) {
            // Mostra mensagem de sucesso e redireciona
            alert('Senha alterada com sucesso!');
            window.location.href = '/login';
        } else {
            return response.json().then(err => {
                throw new Error(err.message || 'Erro ao alterar senha');
            });
        }
    })
    .catch(error => {
        showError(error.message);
    })
    .finally(() => {
        btnSubmit.disabled = false;
        btnSubmit.textContent = 'Redefinir Senha';
    });
});

function showError(message) {
    // Remove mensagens de erro anteriores
    const oldError = document.querySelector('.error-message');
    if (oldError) oldError.remove();
    
    // Cria nova mensagem de erro
    const errorElement = document.createElement('div');
    errorElement.className = 'error-message';
    errorElement.textContent = message;
    
    // Insere após o formulário
    document.querySelector('form').appendChild(errorElement);
}