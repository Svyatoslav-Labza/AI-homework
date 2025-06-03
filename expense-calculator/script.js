// Initialize expenses array
let expenses = [];

// DOM Elements
const expenseForm = document.getElementById('expenseForm');
const categoryInput = document.getElementById('category');
const amountInput = document.getElementById('amount');
const expensesList = document.getElementById('expensesList');
const totalExpensesElement = document.getElementById('totalExpenses');
const averageDailyElement = document.getElementById('averageDaily');
const topExpensesElement = document.getElementById('topExpenses');
const calculateButton = document.getElementById('calculateButton');
const resultsSection = document.getElementById('resultsSection');

// Add expense
expenseForm.addEventListener('submit', (e) => {
    e.preventDefault();
    
    const category = categoryInput.value.trim();
    const amount = parseFloat(amountInput.value);
    
    if (category && amount > 0) {
        addExpense(category, amount);
        
        // Reset form
        categoryInput.value = '';
        amountInput.value = '';
    }
});

// Add calculate button event listener
calculateButton.addEventListener('click', () => {
    if (expenses.length === 0) {
        alert('Please add some expenses first!');
        return;
    }
    updateResults();
    resultsSection.style.display = 'block';
});

// Add expense to the list
function addExpense(category, amount) {
    expenses.push({ category, amount });
    updateExpensesList();
}

// Delete expense
function deleteExpense(index) {
    expenses.splice(index, 1);
    updateExpensesList();
    if (resultsSection.style.display === 'block') {
        updateResults();
    }
    if (expenses.length === 0) {
        resultsSection.style.display = 'none';
    }
}

// Calculate total expenses
function calculateTotal() {
    return expenses.reduce((total, expense) => total + expense.amount, 0);
}

// Calculate average daily expense
function calculateDailyAverage() {
    const total = calculateTotal();
    // Assuming 30 days in a month
    return total / 30;
}

// Get top 3 expenses
function getTopExpenses() {
    return [...expenses]
        .sort((a, b) => b.amount - a.amount)
        .slice(0, 3);
}

// Update expenses list
function updateExpensesList() {
    expensesList.innerHTML = '';
    
    expenses.forEach((expense, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${expense.category}</td>
            <td>$${expense.amount.toFixed(2)}</td>
            <td>
                <button class="delete-btn" onclick="deleteExpense(${index})">Delete</button>
            </td>
        `;
        expensesList.appendChild(row);
    });
}

// Update results
function updateResults() {
    // Update total
    const total = calculateTotal();
    totalExpensesElement.textContent = `$${total.toFixed(2)}`;
    
    // Update daily average
    const dailyAverage = calculateDailyAverage();
    averageDailyElement.textContent = `$${dailyAverage.toFixed(2)}`;
    
    // Update top expenses
    const topExpenses = getTopExpenses();
    topExpensesElement.innerHTML = topExpenses.length > 0
        ? topExpenses.map(expense => `<li>${expense.category}: $${expense.amount.toFixed(2)}</li>`).join('')
        : '<li>-</li><li>-</li><li>-</li>';
}
