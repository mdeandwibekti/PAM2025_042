# 🔴 HTTP 500 Error - Diagnostic Guide

## Status: Your app is correctly configured, the BACKEND has a problem

The Android app is working correctly - it's making the right API call to `GET http://10.0.2.2:3000/api/products`. The server is returning HTTP 500, which means:

1. ✅ Network connection is working
2. ✅ Server is reachable at port 3000
3. ❌ **The `/api/products` endpoint is crashing**

---

## Checklist: Finding the Root Cause

### Step 1: Verify Backend Server is Running
```powershell
# Check if Node.js server is running on port 3000
netstat -ano | findstr :3000

# If nothing shows, start your backend:
cd path/to/backend
npm start
```

**Expected output:** Should see something like `LISTENING` on port 3000

---

### Step 2: Test Backend Endpoint Directly

Use **Postman** or **Insomnia** (or curl in PowerShell):

```powershell
# Test the endpoint
Invoke-WebRequest -Uri "http://localhost:3000/api/products" -Method GET

# Should return 200 with JSON like:
# {
#   "msg": "Success",
#   "data": [
#     {
#       "id": 1,
#       "name": "Produk A",
#       "price": 50000,
#       "stock": 10,
#       ...
#     }
#   ]
# }
```

**If you get 500 error here too**, the problem is definitely in the backend.

---

### Step 3: Check Backend Logs

Look for errors like:

```
// ❌ Common Errors in Backend Console:

1. Database Connection Error
   "ECONNREFUSED: Connection refused to MySQL"
   → Solution: Start MySQL/MariaDB

2. Table Not Found
   "ER_NO_SUCH_TABLE: Table 'database.products' doesn't exist"
   → Solution: Create products table

3. Null Reference Error
   "Cannot read property 'length' of undefined"
   → Solution: Database query returned null

4. Syntax Error in Query
   "ER_PARSE_ERROR: You have an error in your SQL syntax"
   → Solution: Fix the SQL query in routes
```

---

### Step 4: Verify Database

```sql
-- Check if products table exists
SHOW TABLES;

-- Check table structure
DESCRIBE products;

-- Check if data exists
SELECT COUNT(*) FROM products;

-- If table doesn't exist, create it:
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    stock INT NOT NULL,
    description TEXT,
    image VARCHAR(255),
    seller_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample data:
INSERT INTO products (name, price, stock, description) VALUES
('Laptop Gaming', 15000000, 5, 'Laptop untuk gaming'),
('Keyboard Mechanical', 500000, 20, 'Keyboard gaming'),
('Mouse Wireless', 200000, 15, 'Mouse gaming RGB');
```

---

### Step 5: Fix Backend Route

Your backend `/api/products` route should look like this:

```javascript
// routes/products.js or similar
router.get('/api/products', async (req, res) => {
    try {
        const products = await Product.findAll(); // or your query method
        
        res.json({
            msg: "Success",
            data: products
        });
    } catch (error) {
        console.error('❌ Error fetching products:', error);
        res.status(500).json({
            msg: "Error",
            data: [],
            error: error.message
        });
    }
});
```

**Key points:**
- ✅ Wraps in try-catch
- ✅ Returns JSON with `msg` and `data` fields
- ✅ Logs error to console
- ✅ Has error handling

---

## Quick Diagnostics Checklist

- [ ] Backend server is running (`npm start`)
- [ ] MySQL/Database is running
- [ ] Products table exists in database
- [ ] Products table has data
- [ ] `/api/products` endpoint returns 200 status
- [ ] Response JSON has `msg` and `data` fields
- [ ] Backend console shows no errors

---

## Android App Status: ✅ All Good

Your Android code is correct:

✅ Retrofit configured correctly  
✅ API endpoint path is correct  
✅ Response model (ProductResponse) matches backend  
✅ HomeViewModel handles errors correctly  
✅ Error is being logged: "Error loading products"  

**The issue is 100% on the backend side.**

---

## Next Steps

1. **Check backend running status** - Is your Express server actually started?
2. **Check database running status** - Is MySQL running?
3. **Test endpoint with Postman** - Confirm 500 error is consistent
4. **Check backend logs** - Look for the actual error message
5. **Fix the identified issue** - Once you know what's wrong, fix it
6. **Test again with Postman** - Verify you get 200 + correct JSON
7. **Test from Android app** - If Postman works, Android will work too

---

## Network Address Notes

- **Android Emulator:** `10.0.2.2:3000` = localhost:3000 on your machine ✅
- This is correctly configured in `ContainerApp.kt`


