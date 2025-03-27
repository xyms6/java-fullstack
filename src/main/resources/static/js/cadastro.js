document.getElementById('cadastroForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const usuarioDTO = {
        nome: document.getElementById('nome').value,
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value
    };
    
    fetch('/api/auth/registrar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuarioDTO)
    })
    .then(response => {
        if (response.ok) {
            alert('Cadastro realizado com sucesso!');
            window.location.href = '/login';
        } else {
            return response.json().then(err => {
                throw new Error(err.message || 'Erro no cadastro');
            });
        }
    })
    .catch(error => {
        alert(error.message);
    });
});