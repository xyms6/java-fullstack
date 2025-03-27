document.getElementById('recuperarSenhaForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const email = document.getElementById('email').value;
    const btnSubmit = document.querySelector('#recuperarSenhaForm button[type="submit"]');
    
    // Desabilita o botão para evitar múltiplos envios
    btnSubmit.disabled = true;
    btnSubmit.textContent = 'Enviando...';
    
    fetch(`/api/auth/recuperar-senha?email=${encodeURIComponent(email)}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            // Mostra mensagem de sucesso
            const successMessage = document.createElement('div');
            successMessage.className = 'success-message';
            successMessage.textContent = 'Um link de recuperação foi enviado para seu email.';
            document.querySelector('main').appendChild(successMessage);
            
            // Limpa o formulário
            document.getElementById('recuperarSenhaForm').reset();
        } else {
            return response.json().then(err => {
                throw new Error(err.message || 'Erro ao solicitar recuperação de senha');
            });
        }
    })
    .catch(error => {
        // Mostra mensagem de erro
        const errorElement = document.querySelector('.error-message') || document.createElement('div');
        errorElement.className = 'error-message';
        errorElement.textContent = error.message;
        document.querySelector('main').appendChild(errorElement);
    })
    .finally(() => {
        btnSubmit.disabled = false;
        btnSubmit.textContent = 'Enviar Link de Recuperação';
    });
});