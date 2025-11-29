import React, { useState, useEffect } from 'react';
import { Modal, Form, Button, Alert } from 'react-bootstrap';
import api from '../utils/axiosConfig';

const DataPackageModal = ({ show, handleClose, packageData, dataSourceId, onSuccess }) => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    dataType: 'RAW_DATA',
    format: 'CSV',
    fileSize: '',
    price: '',
    pricingModel: 'ONE_TIME',
    status: 'PENDING_APPROVAL',
    filePath: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (packageData) {
      setFormData({
        name: packageData.name || '',
        description: packageData.description || '',
        dataType: packageData.dataType || 'RAW_DATA',
        format: packageData.format || 'CSV',
        fileSize: packageData.size || '',
        price: packageData.price || '',
        pricingModel: packageData.pricingModel || 'ONE_TIME',
        status: packageData.status || 'PENDING_APPROVAL',
        filePath: packageData.filePath || ''
      });
    } else {
      resetForm();
    }
  }, [packageData, show]);

  const resetForm = () => {
    setFormData({
      name: '',
      description: '',
      dataType: 'RAW_DATA',
      format: 'CSV',
      fileSize: '',
      price: '',
      pricingModel: 'ONE_TIME',
      status: 'PENDING_APPROVAL',
      filePath: ''
    });
    setError('');
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      if (packageData && packageData.id) {
        // Update existing package
        const updatePayload = {
          name: formData.name,
          description: formData.description,
          dataType: formData.dataType,
          format: formData.format,
          size: parseInt(formData.fileSize) || 0,
          price: parseFloat(formData.price),
          pricingModel: formData.pricingModel,
          status: formData.status,
        };
        await api.put(`/api/data-packages/${packageData.id}`, updatePayload);
      } else {
        // Create new package
        const createPayload = {
            name: formData.name,
            description: formData.description,
            dataType: formData.dataType,
            format: formData.format,
            size: parseInt(formData.fileSize) || 0,
            price: parseFloat(formData.price),
            pricingModel: formData.pricingModel,
            status: formData.status,
            filePath: formData.filePath,
            dataSource: {
              id: dataSourceId
            }
        };
        await api.post('/api/data-packages', createPayload);
      }

      if (onSuccess) {
        onSuccess();
      }
      handleClose();
      resetForm();
    } catch (err) {
      setError(err.response?.data?.error || 'Có lỗi xảy ra khi lưu gói dữ liệu');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={handleClose} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>
          {packageData ? 'Chỉnh sửa gói dữ liệu' : 'Tạo gói dữ liệu mới'}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {error && <Alert variant="danger">{error}</Alert>}
        
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Tên gói dữ liệu *</Form.Label>
            <Form.Control
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              placeholder="Nhập tên gói dữ liệu..."
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Mô tả *</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              name="description"
              value={formData.description}
              onChange={handleChange}
              required
              placeholder="Mô tả chi tiết về gói dữ liệu..."
            />
          </Form.Group>

          <div className="row">
            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>Loại dữ liệu *</Form.Label>
                <Form.Select
                  name="dataType"
                  value={formData.dataType}
                  onChange={handleChange}
                  required
                >
                  <option value="RAW_DATA">Dữ liệu thô</option>
                  <option value="PROCESSED_DATA">Dữ liệu đã xử lý</option>
                  <option value="ANALYTICS_REPORT">Báo cáo phân tích</option>
                  <option value="DASHBOARD">Dashboard</option>
                </Form.Select>
              </Form.Group>
            </div>

            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>Định dạng *</Form.Label>
                <Form.Select
                  name="format"
                  value={formData.format}
                  onChange={handleChange}
                  required
                >
                  <option value="CSV">CSV</option>
                  <option value="JSON">JSON</option>
                  <option value="XML">XML</option>
                  <option value="EXCEL">Excel</option>
                  <option value="PDF">PDF</option>
                  <option value="API">API</option>
                </Form.Select>
              </Form.Group>
            </div>
          </div>

          <div className="row">
            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>Kích thước file (bytes)</Form.Label>
                <Form.Control
                  type="number"
                  name="fileSize"
                  value={formData.fileSize}
                  onChange={handleChange}
                  placeholder="Ví dụ: 10485760 (10MB)"
                />
                <Form.Text className="text-muted">
                  Để trống nếu là API hoặc chưa xác định
                </Form.Text>
              </Form.Group>
            </div>

            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>Giá (VND) *</Form.Label>
                <Form.Control
                  type="number"
                  name="price"
                  value={formData.price}
                  onChange={handleChange}
                  required
                  min="0"
                  step="0.01"
                  placeholder="Nhập giá..."
                />
              </Form.Group>
            </div>
          </div>

          <div className="row">
            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>Mô hình giá *</Form.Label>
                <Form.Select
                  name="pricingModel"
                  value={formData.pricingModel}
                  onChange={handleChange}
                  required
                >
                  <option value="ONE_TIME">Một lần</option>
                  <option value="SUBSCRIPTION">Thuê bao</option>
                  <option value="PER_DOWNLOAD">Theo lượt tải</option>
                </Form.Select>
              </Form.Group>
            </div>

            <div className="col-md-6">
              <Form.Group className="mb-3">
                <Form.Label>Trạng thái</Form.Label>
                <Form.Select
                  name="status"
                  value={formData.status}
                  onChange={handleChange}
                >
                  <option value="PENDING_APPROVAL">Chờ duyệt</option>
                  <option value="ACTIVE">Hoạt động</option>
                  <option value="INACTIVE">Không hoạt động</option>
                </Form.Select>
                <Form.Text className="text-muted">
                  Gói mới sẽ cần được admin duyệt
                </Form.Text>
              </Form.Group>
            </div>
          </div>

          <Form.Group className="mb-3">
            <Form.Label>Đường dẫn file</Form.Label>
            <Form.Control
              type="text"
              name="filePath"
              value={formData.filePath}
              onChange={handleChange}
              placeholder="Ví dụ: /data/packages/file.csv"
            />
            <Form.Text className="text-muted">
              Đường dẫn đến file dữ liệu trên server
            </Form.Text>
          </Form.Group>

          <div className="d-flex justify-content-end gap-2">
            <Button variant="secondary" onClick={handleClose} disabled={loading}>
              Hủy
            </Button>
            <Button variant="primary" type="submit" disabled={loading}>
              {loading ? 'Đang lưu...' : (packageData ? 'Cập nhật' : 'Tạo mới')}
            </Button>
          </div>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default DataPackageModal;

