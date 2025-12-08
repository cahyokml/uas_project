<?php
// api.php - Event Management REST API with MySQL Database
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Database Configuration
class Database {
    private $host = 'localhost';
    private $dbName = 'event_management';
    private $username = 'root';
    private $password = '';
    private $conn = null;
    
    public function connect() {
        if ($this->conn === null) {
            try {
                $this->conn = new PDO(
                    "mysql:host={$this->host};dbname={$this->dbName};charset=utf8mb4",
                    $this->username,
                    $this->password,
                    [
                        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC
                    ]
                );
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode([
                    'status' => 500,
                    'message' => 'Database connection failed: ' . $e->getMessage()
                ]);
                exit();
            }
        }
        return $this->conn;
    }
}

// Event Model
class Event {
    private $db;
    private $table = 'events';
    
    public function __construct($db) {
        $this->db = $db;
    }
    
    public function getAll($filters = []) {
        $sql = "SELECT * FROM {$this->table}";
        $conditions = [];
        $params = [];
        
        if (!empty($filters['status'])) {
            $conditions[] = "status = :status";
            $params[':status'] = $filters['status'];
        }
        
        if (!empty($filters['date_from'])) {
            $conditions[] = "date >= :date_from";
            $params[':date_from'] = $filters['date_from'];
        }
        
        if (!empty($filters['date_to'])) {
            $conditions[] = "date <= :date_to";
            $params[':date_to'] = $filters['date_to'];
        }
        
        if (!empty($conditions)) {
            $sql .= " WHERE " . implode(' AND ', $conditions);
        }
        
        $sql .= " ORDER BY date ASC, time ASC";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute($params);
        return $stmt->fetchAll();
    }
    
    public function getById($id) {
        $sql = "SELECT * FROM {$this->table} WHERE id = :id";
        $stmt = $this->db->prepare($sql);
        $stmt->execute([':id' => $id]);
        return $stmt->fetch();
    }
    
    public function create($data) {
        $sql = "INSERT INTO {$this->table} 
                (title, date, time, location, description, capacity, status) 
                VALUES 
                (:title, :date, :time, :location, :description, :capacity, :status)";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute([
            ':title' => $data['title'],
            ':date' => $data['date'],
            ':time' => $data['time'],
            ':location' => $data['location'],
            ':description' => $data['description'] ?? '',
            ':capacity' => $data['capacity'] ?? null,
            ':status' => $data['status']
        ]);
        
        return $this->getById($this->db->lastInsertId());
    }
    
    public function update($id, $data) {
        $sql = "UPDATE {$this->table} SET 
                title = :title,
                date = :date,
                time = :time,
                location = :location,
                description = :description,
                capacity = :capacity,
                status = :status,
                updated_at = CURRENT_TIMESTAMP
                WHERE id = :id";
        
        $stmt = $this->db->prepare($sql);
        $result = $stmt->execute([
            ':id' => $id,
            ':title' => $data['title'],
            ':date' => $data['date'],
            ':time' => $data['time'],
            ':location' => $data['location'],
            ':description' => $data['description'] ?? '',
            ':capacity' => $data['capacity'] ?? null,
            ':status' => $data['status']
        ]);
        
        return $result ? $this->getById($id) : false;
    }
    
    public function delete($id) {
        $sql = "DELETE FROM {$this->table} WHERE id = :id";
        $stmt = $this->db->prepare($sql);
        return $stmt->execute([':id' => $id]);
    }
    
    public function getStatistics() {
        $sql = "SELECT 
                COUNT(*) as total,
                SUM(CASE WHEN status = 'upcoming' THEN 1 ELSE 0 END) as upcoming,
                SUM(CASE WHEN status = 'ongoing' THEN 1 ELSE 0 END) as ongoing,
                SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END) as completed,
                SUM(CASE WHEN status = 'cancelled' THEN 1 ELSE 0 END) as cancelled
                FROM {$this->table}";
        
        $stmt = $this->db->prepare($sql);
        $stmt->execute();
        return $stmt->fetch();
    }
}

// Helper Functions
function sendResponse($status, $data = null, $message = null) {
    http_response_code($status);
    echo json_encode([
        'status' => $status,
        'message' => $message,
        'data' => $data,
        'timestamp' => date('Y-m-d H:i:s')
    ]);
    exit();
}

function validateEvent($data, $isUpdate = false) {
    $required = ['title', 'date', 'time', 'location', 'status'];
    
    foreach ($required as $field) {
        if (empty($data[$field])) {
            return "Field '$field' is required";
        }
    }
    
    if (!preg_match('/^\d{4}-\d{2}-\d{2}$/', $data['date'])) {
        return "Invalid date format. Use YYYY-MM-DD";
    }
    
    if (!preg_match('/^\d{2}:\d{2}(:\d{2})?$/', $data['time'])) {
        return "Invalid time format. Use HH:MM or HH:MM:SS";
    }
    
    $validStatuses = ['upcoming', 'ongoing', 'completed', 'cancelled'];
    if (!in_array($data['status'], $validStatuses)) {
        return "Invalid status. Must be one of: " . implode(', ', $validStatuses);
    }
    
    if (isset($data['capacity']) && (!is_numeric($data['capacity']) || $data['capacity'] < 0)) {
        return "Capacity must be a positive number";
    }
    
    return null;
}

// Main Application
try {
    $database = new Database();
    $db = $database->connect();
    $eventModel = new Event($db);
    
    $method = $_SERVER['REQUEST_METHOD'];
    $input = json_decode(file_get_contents('php://input'), true);
    $id = $_GET['id'] ?? null;
    
    switch ($method) {
        case 'GET':
            if ($id) {
                // Get single event
                $event = $eventModel->getById($id);
                if (!$event) {
                    sendResponse(404, null, 'Event not found');
                }
                sendResponse(200, $event, 'Event retrieved successfully');
            } elseif (isset($_GET['stats'])) {
                // Get statistics
                $stats = $eventModel->getStatistics();
                sendResponse(200, $stats, 'Statistics retrieved successfully');
            } else {
                // Get all events with optional filters
                $filters = [
                    'status' => $_GET['status'] ?? null,
                    'date_from' => $_GET['date_from'] ?? null,
                    'date_to' => $_GET['date_to'] ?? null
                ];
                $events = $eventModel->getAll($filters);
                sendResponse(200, $events, 'Events retrieved successfully');
            }
            break;
        
        case 'POST':
            if (!$input) {
                sendResponse(400, null, 'Invalid JSON data');
            }
            
            $error = validateEvent($input);
            if ($error) {
                sendResponse(400, null, $error);
            }
            
            $newEvent = $eventModel->create($input);
            sendResponse(201, $newEvent, 'Event created successfully');
            break;
        
        case 'PUT':
            if (!$id) {
                sendResponse(400, null, 'Event ID is required');
            }
            
            if (!$input) {
                sendResponse(400, null, 'Invalid JSON data');
            }
            
            $error = validateEvent($input, true);
            if ($error) {
                sendResponse(400, null, $error);
            }
            
            $event = $eventModel->getById($id);
            if (!$event) {
                sendResponse(404, null, 'Event not found');
            }
            
            $updatedEvent = $eventModel->update($id, $input);
            sendResponse(200, $updatedEvent, 'Event updated successfully');
            break;
        
        case 'DELETE':
            if (!$id) {
                sendResponse(400, null, 'Event ID is required');
            }
            
            $event = $eventModel->getById($id);
            if (!$event) {
                sendResponse(404, null, 'Event not found');
            }
            
            $eventModel->delete($id);
            sendResponse(200, null, 'Event deleted successfully');
            break;
        
        default:
            sendResponse(405, null, 'Method not allowed');
    }
    
} catch (Exception $e) {
    sendResponse(500, null, 'Server error: ' . $e->getMessage());
}
?>