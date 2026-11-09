const API_URL = '/api/laptops';

document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById('laptop-list-body')) {
        fetchLaptops();
    }

    if (document.getElementById('laptop-form')) {
        checkEditMode();
    }
});

// --- LOGIC TRANG DANH SÁCH (index.html) ---
function fetchLaptops() {
    fetch(API_URL)
        .then(res => res.json())
        .then(data => renderTable(data))
        .catch(err => console.error("Error fetching laptops:", err));
}

function renderTable(laptops) {
    const tbody = document.getElementById('laptop-list-body');
    tbody.innerHTML = '';
    laptops.forEach(laptop => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${laptop.id}</td>
            <td><img src="${laptop.imageUrl || 'https://via.placeholder.com/50'}" alt="img" height="50" style="object-fit: contain; border-radius: 4px;"></td>
            <td><strong>${laptop.name}</strong></td>
            <td>${laptop.brand || ''}</td>
            <td style="color: #dc3545; font-weight: bold;">$${laptop.price}</td>
            <td>${laptop.quantity}</td>
            <td>
                <a class="btn-edit" href="add.html?id=${laptop.id}" style="text-decoration:none; display:inline-block;">Sửa</a>
                <button class="btn-delete" onclick="deleteLaptop(${laptop.id})">Xóa</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function deleteLaptop(id) {
    if (confirm("Bạn có chắc muốn xóa laptop này?")) {
        fetch(`${API_URL}/${id}`, { method: 'DELETE' })
            .then(() => fetchLaptops())
            .catch(err => console.error("Error deleting:", err));
    }
}

// --- LOGIC TRANG THÊM/SỬA (add.html) ---
function checkEditMode() {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    if (id) {
        document.getElementById('page-title').innerText = "Cập nhật Laptop";
        fetch(`${API_URL}/${id}`)
            .then(res => res.json())
            .then(laptop => {
                document.getElementById('laptop-id').value = laptop.id;
                document.getElementById('name').value = laptop.name;
                document.getElementById('brand').value = laptop.brand;
                document.getElementById('price').value = laptop.price;
                document.getElementById('quantity').value = laptop.quantity;
                document.getElementById('description').value = laptop.description || '';
                document.getElementById('existingImageUrl').value = laptop.imageUrl || '';
            });
    }
}

async function saveLaptop() {
    const id = document.getElementById('laptop-id').value;
    let imageUrl = document.getElementById('existingImageUrl').value;
    const fileInput = document.getElementById('imageFile');

    try {
        if (fileInput.files.length > 0) {
            const formData = new FormData();
            formData.append('fileName', fileInput.files[0].name);
            formData.append('file', fileInput.files[0]);

            // Do not set Content-Type manually; the browser adds the multipart boundary.
            const uploadRes = await fetch('/api/upload', {
                method: 'POST',
                body: formData
            });
            if (!uploadRes.ok) {
                const errorText = await uploadRes.text();
                console.error('Image upload failed:', uploadRes.status, errorText);
                alert(`Lỗi upload ảnh (${uploadRes.status}): ${errorText || 'Vui lòng thử lại.'}`);
                return;
            }
            imageUrl = (await uploadRes.text()).trim();
        }

        const laptop = {
            name: document.getElementById('name').value,
            brand: document.getElementById('brand').value,
            price: parseFloat(document.getElementById('price').value),
            quantity: parseInt(document.getElementById('quantity').value),
            description: document.getElementById('description').value,
            imageUrl: imageUrl
        };

        const method = id ? 'PUT' : 'POST';
        const url = id ? `${API_URL}/${id}` : API_URL;
        const saveRes = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(laptop)
        });
        if (!saveRes.ok) {
            const errorText = await saveRes.text();
            throw new Error(`Lưu sản phẩm thất bại (${saveRes.status}): ${errorText}`);
        }
        window.location.href = 'index.html';
    } catch (err) {
        console.error("Error saving laptop:", err);
        alert(`Không thể lưu sản phẩm: ${err.message || 'Lỗi kết nối máy chủ.'}`);
    }
}
