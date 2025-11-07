import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button, Badge, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import axios from 'axios';

const DataPackages = () => {
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchFilters, setSearchFilters] = useState({
    name: '',
    dataType: '',
    format: '',
    minPrice: '',
    maxPrice: ''
  });

  useEffect(() => {
    fetchDataPackages();
  }, []);

  const fetchDataPackages = async () => {
    try {
      const response = await axios.get('/api/data-packages');
      setPackages(response.data);
    } catch (err) {
      setError('Không thể tải danh sách dữ liệu');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    try {
      setLoading(true);
      const params = new URLSearchParams();
      
      Object.keys(searchFilters).forEach(key => {
        if (searchFilters[key]) {
          params.append(key, searchFilters[key]);
        }
      });

      const response = await axios.get(`/api/data-packages/search?${params}`);
      setPackages(response.data);
    } catch (err) {
      setError('Không thể tìm kiếm dữ liệu');
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (e) => {
    setSearchFilters({
      ...searchFilters,
      [e.target.name]: e.target.value
    });
  };

  const getStatusBadge = (status) => {
    const statusMap = {
      'ACTIVE': { variant: 'success', text: 'Hoạt động' },
      'INACTIVE': { variant: 'danger', text: 'Không hoạt động' },
      'PENDING_APPROVAL': { variant: 'warning', text: 'Chờ duyệt' }
    };
    
    const statusInfo = statusMap[status] || { variant: 'secondary', text: status };
    return <Badge bg={statusInfo.variant}>{statusInfo.text}</Badge>;
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(price);
  };

  const formatFileSize = (bytes) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  };

  if (loading) {
    return (
      <Container className="mt-4">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Đang tải...</span>
          </div>
        </div>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <Row>
        <Col>
          <h2>Danh sách gói dữ liệu</h2>
          <p className="text-muted">Tìm kiếm và khám phá các gói dữ liệu EV có sẵn</p>
        </Col>
      </Row>

      {error && <Alert variant="danger">{error}</Alert>}

      <Row className="mb-4">
        <Col>
          <Card className="search-form">
            <Card.Body>
              <Row>
                <Col md={3}>
                  <Form.Group>
                    <Form.Label>Tên gói dữ liệu</Form.Label>
                    <Form.Control
                      type="text"
                      name="name"
                      value={searchFilters.name}
                      onChange={handleFilterChange}
                      placeholder="Nhập tên gói dữ liệu..."
                    />
                  </Form.Group>
                </Col>
                <Col md={2}>
                  <Form.Group>
                    <Form.Label>Loại dữ liệu</Form.Label>
                    <Form.Select
                      name="dataType"
                      value={searchFilters.dataType}
                      onChange={handleFilterChange}
                    >
                      <option value="">Tất cả</option>
                      <option value="RAW_DATA">Dữ liệu thô</option>
                      <option value="PROCESSED_DATA">Dữ liệu đã xử lý</option>
                      <option value="ANALYTICS_REPORT">Báo cáo phân tích</option>
                      <option value="DASHBOARD">Dashboard</option>
                    </Form.Select>
                  </Form.Group>
                </Col>
                <Col md={2}>
                  <Form.Group>
                    <Form.Label>Định dạng</Form.Label>
                    <Form.Select
                      name="format"
                      value={searchFilters.format}
                      onChange={handleFilterChange}
                    >
                      <option value="">Tất cả</option>
                      <option value="CSV">CSV</option>
                      <option value="JSON">JSON</option>
                      <option value="EXCEL">Excel</option>
                      <option value="PDF">PDF</option>
                    </Form.Select>
                  </Form.Group>
                </Col>
                <Col md={2}>
                  <Form.Group>
                    <Form.Label>Giá tối thiểu</Form.Label>
                    <Form.Control
                      type="number"
                      name="minPrice"
                      value={searchFilters.minPrice}
                      onChange={handleFilterChange}
                      placeholder="0"
                    />
                  </Form.Group>
                </Col>
                <Col md={2}>
                  <Form.Group>
                    <Form.Label>Giá tối đa</Form.Label>
                    <Form.Control
                      type="number"
                      name="maxPrice"
                      value={searchFilters.maxPrice}
                      onChange={handleFilterChange}
                      placeholder="1000000"
                    />
                  </Form.Group>
                </Col>
                <Col md={1} className="d-flex align-items-end">
                  <Button variant="primary" onClick={handleSearch} className="w-100">
                    Tìm kiếm
                  </Button>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        {packages.map((pkg) => (
          <Col md={6} lg={4} key={pkg.id} className="mb-4">
            <Card className="data-package-card h-100">
              <Card.Header className="d-flex justify-content-between align-items-center">
                <h6 className="mb-0">{pkg.name}</h6>
                {getStatusBadge(pkg.status)}
              </Card.Header>
              <Card.Body>
                <p className="card-text">{pkg.description}</p>
                <div className="mb-2">
                  <small className="text-muted">
                    <strong>Loại:</strong> {pkg.dataType?.replace('_', ' ')}<br/>
                    <strong>Định dạng:</strong> {pkg.format}<br/>
                    <strong>Kích thước:</strong> {formatFileSize(pkg.size || 0)}
                  </small>
                </div>
                <div className="d-flex justify-content-between align-items-center">
                  <span className="price-tag">{formatPrice(pkg.price)}</span>
                  <Button as={Link} to={`/data-packages/${pkg.id}`} variant="outline-primary" size="sm">
                    Xem chi tiết
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      {packages.length === 0 && !loading && (
        <Row>
          <Col>
            <Alert variant="info" className="text-center">
              Không tìm thấy gói dữ liệu nào phù hợp với tiêu chí tìm kiếm.
            </Alert>
          </Col>
        </Row>
      )}
    </Container>
  );
};

export default DataPackages;
