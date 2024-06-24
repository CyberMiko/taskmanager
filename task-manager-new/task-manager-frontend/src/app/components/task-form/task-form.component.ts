import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.css']
})
export class TaskFormComponent implements OnInit {
  task: Task = new Task();
  isEditMode: boolean = false;

  constructor(private taskService: TaskService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.taskService.getTask(+id).subscribe((task: Task) => {
        this.task = task;
      });
    }
  }

  onSubmit(): void {
    if (this.isEditMode) {
      this.taskService.updateTask(this.task.id, this.task).subscribe(() => {
        this.router.navigate(['/']);
      });
    } else {
      this.taskService.createTask(this.task).subscribe(() => {
        this.router.navigate(['/']);
      });
    }
  }
}
