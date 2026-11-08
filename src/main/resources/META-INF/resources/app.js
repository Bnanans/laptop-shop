const API_URL = '/laptops';

document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById('product-grid')) {
        loadLaptops();
    }
    updateCartBadge();
    renderAuthSection();
});

let allLaptops = [];
let currentPage = 1;
const itemsPerPage = 10;

function loadLaptops(keyword = '') {
    const url = keyword ? `${API_URL}/search/${keyword}` : API_URL;

    fetch(url)
        .then(res => res.json())
        .then(data => {
            const urlParams = new URLSearchParams(window.location.search);
            const type = urlParams.get('type');

            if (type && !keyword) {
                if (type.toLowerCase() === 'khac') {
                    const knownBrands = ['mac', 'asus', 'lenovo', 'dell', 'hp', 'acer', 'gigabyte', 'huawei'];
                    allLaptops = data.filter(l => l.brand && !knownBrands.includes(l.brand.toLowerCase()));
                } else {
                    allLaptops = data.filter(l => l.brand && l.brand.toLowerCase() === type.toLowerCase());
                }
            } else {
                allLaptops = data;
            }
            renderPage(1);
        })
        .catch(err => console.error("Lỗi tải dữ liệu:", err));
}

function renderPage(page) {
    currentPage = page;
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    renderGrid(allLaptops.slice(start, end));
    renderPagination();
}

function renderPagination() {
    const totalPages = Math.ceil(allLaptops.length / itemsPerPage);
    const container = document.getElementById('pagination-container');
    if (!container) return;

    if (totalPages <= 1) {
        container.innerHTML = ''; return;
    }

    let html = '';
    for (let i = 1; i <= totalPages; i++) {
        html += `<button class="${i === currentPage ? 'active' : ''}" onclick="renderPage(${i})">${i}</button>`;
    }
    container.innerHTML = html;
}

function renderGrid(laptops) {
    const grid = document.getElementById('product-grid');
    grid.innerHTML = '';
    laptops.forEach(laptop => {
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
            <div onclick="location.href='/detail.html?id=${laptop.id}'" style="cursor:pointer; display:flex; flex-direction:column; flex:1;">
                <img src="${laptop.imageUrl || 'https://via.placeholder.com/150'}" alt="${laptop.name}">
                <h3>${laptop.name}</h3>
                <p>Hãng: ${laptop.brand || 'N/A'}</p>
                <p class="price">$${laptop.price}</p>
            </div>
            <button class="btn-cart" onclick="addToCart(${laptop.id})">Thêm vào giỏ</button>
        `;
        grid.appendChild(card);
    });
}

function search() {
    const keyword = document.getElementById('keyword').value;
    loadLaptops(keyword);
}

async function addToCart(id) {
    const user = JSON.parse(localStorage.getItem('currentUser'));
    if (!user) {
        alert("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
        window.location.href = '/login.html';
        return;
    }

    try {
        const res = await fetch(`/laptops/${id}`);
        if (!res.ok) throw new Error("Product not found");
        const laptop = await res.json();

        if (laptop.quantity <= 0) {
            alert("Rất tiếc, sản phẩm hiện đang hết hàng!");
            return;
        }

        const cartKey = 'cart_' + user.email;
        let cart = JSON.parse(localStorage.getItem(cartKey)) || [];
        if (cart.length > 0 && typeof cart[0] === 'number') { cart = []; }

        let item = cart.find(c => c.id === id);
        if (item) {
            if (item.qty >= laptop.quantity) {
                alert(`Bạn đã đạt số lượng tối đa (${laptop.quantity}) có sẵn trong kho cho sản phẩm này!`);
                return;
            }
            item.qty += 1;
        } else {
            cart.push({ id: id, qty: 1 });
        }

        localStorage.setItem(cartKey, JSON.stringify(cart));
        updateCartBadge();
        alert("Đã thêm sản phẩm vào giỏ hàng!");
    } catch (e) {
        console.error("Lỗi:", e);
        alert("Không thể kết nối đến máy chủ để kiểm tra kho hàng.");
    }
}

function updateCartBadge() {
    const user = JSON.parse(localStorage.getItem('currentUser'));
    const badge = document.getElementById('cart-badge');
    if (!user) {
        if (badge) badge.innerText = 0;
        return;
    }
    const cartKey = 'cart_' + user.email;
    const cart = JSON.parse(localStorage.getItem(cartKey)) || [];
    if (cart.length > 0 && typeof cart[0] === 'number') { localStorage.removeItem(cartKey); return; }
    const total = cart.reduce((sum, item) => sum + item.qty, 0);
    if (badge) badge.innerText = total;
}

function renderAuthSection() {
    const authSection = document.getElementById('auth-section');
    const user = JSON.parse(localStorage.getItem('currentUser'));

    if (user) {
        authSection.innerHTML = `
            <div class="account-dropdown">
                <div class="account-name" onclick="location.href='/account.html'">👤 ${user.name} ▾</div>
                <div class="dropdown-content">
                    <p>SĐT: ${user.phone}</p>
                    <a href="/account.html">Sửa thông tin</a>
                    ${user.isAdmin ? '<a href="/admin/index.html" class="admin-link">⚙️ Trang Quản Trị</a>' : ''}
                    <a class="logout-link" onclick="logout()">Đăng xuất</a>
                </div>
            </div>
        `;
    } else {
        authSection.innerHTML = `<a href="/login.html" class="btn-login">Đăng nhập</a> <a href="/register.html" class="btn-register">Đăng ký</a>`;
    }
}

function logout() {
    localStorage.removeItem('currentUser');
    window.location.reload();
}